package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.domain.*;
import cn.wolfcode.p2p.business.mapper.BidRequestMapper;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.*;
import cn.wolfcode.p2p.util.*;
import com.sun.org.apache.bcel.internal.generic.IREM;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.text.resources.cldr.ar.FormatData_ar_TN;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class BidRequestServiceImpl implements IBidRequestService {

    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IActionMessageService actionMessageService;


    public void apply(BidRequest br) {

        //参数判断
        //借款金额最小判断
        if (br.getBidRequestAmount().compareTo(Constants.BORROW_MIN_AMOUNT) < 0 ){
            throw new DisplayableException("最小借款金额:"+Constants.BORROW_MIN_AMOUNT);
        }
        //借款金额最大判断
        LoginInfo loginInfo = UserContext.getLoginInfo();
        Account account = accountService.get(loginInfo.getId());
        if (br.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) > 0) {
            throw new DisplayableException("最大借款金额:"+account.getRemainBorrowLimit());
        }

        //最小利率
        if (br.getCurrentRate().compareTo(Constants.BORROW_MIN_RATE) < 0) {
            throw new DisplayableException("最小借款利率:"+Constants.BORROW_MIN_RATE);
        }
        //最大利率
        if (br.getCurrentRate().compareTo(Constants.BORROW_MAX_RATE) > 0) {
            throw new DisplayableException("最大借款利率:"+Constants.BORROW_MAX_RATE);
        }

        //最小投标下限值
        if(br.getMinBidAmount().compareTo(Constants.BORROW_MIN_AMOUNT) < 0){
            throw new DisplayableException("最小投标金额:"+Constants.BORROW_MIN_AMOUNT);
        }

        //判断用户是否已经有一个借款在申请中
        UserInfo userInfo = userInfoService.get(loginInfo.getId());
        if (userInfo.isBidRequestProcess()){
            throw new DisplayableException("您已经有借款在申请流程中");
        }

        //保存一个bidRequest对象
        BidRequest bidRequest = new BidRequest();
        bidRequest.setApplyTime(new Date());
        bidRequest.setBidRequestAmount(br.getBidRequestAmount());
        bidRequest.setBidRequestState(br.getBidRequestState());
        bidRequest.setBidRequestType(br.getBidRequestType());
        bidRequest.setCreateUser(loginInfo);
        bidRequest.setCurrentRate(br.getCurrentRate());
        bidRequest.setDescription(br.getDescription());
        bidRequest.setDisableDate(br.getDisableDate());
        bidRequest.setDisableDays(br.getDisableDays());
        bidRequest.setMinBidAmount(br.getMinBidAmount());
        bidRequest.setMonthes2Return(br.getMonthes2Return());
        bidRequest.setReturnType(br.getReturnType());
        bidRequest.setTitle(br.getTitle());
        bidRequest.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),
                br.getBidRequestAmount(),br.getCurrentRate(),br.getMonthes2Return()));
        //保存到数据库
        bidRequestMapper.insert(bidRequest);

        //给用户增加是否有申请中的借款状态
        Long tempState = BitStatesUtils.addState(userInfo.getBitState(), BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
        userInfo.setBitState(tempState);
        //提交贷款申请同时改变状态
        userInfoService.update(userInfo);
    }


    public PageResult query(BidRequestQueryObject qo) {

        int count = bidRequestMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }
        List<BidRequest> list = bidRequestMapper.queryForList(qo);
        return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
    }

    public void publishAudit(Long id, String remark, int state, Date publishTime) {
        //判断借款对象状态处于待审核
        BidRequest br = bidRequestMapper.selectByPrimaryKey(id);
        if (br.getBidRequestState() != Constants.BIDREQUEST_STATE_APPLY) {
            throw new DisplayableException("借款状态不处于待审核");
        }

        //设置审核相关信息
        bidRequestAuditHistoryService.save(br,BidRequestAuditHistory.TYPE_PUBLISH,remark,state);

        //审核成功
        if (state == BidRequestAuditHistory.STATE_SUCCESS) {
            if (publishTime == null) {
                //如果没有指定发标时间,借款状态修改为招标中
                br.setBidRequestState(Constants.BIDREQUEST_STATE_BIDDING);//招标中
                br.setPublishTime(new Date());
            }else {
                //如果有指定发标时间,借款状态处于待发标
                br.setBidRequestState(Constants.BIDREQUEST_STATE_PUBLISH_PENDING);
                br.setPublishTime(publishTime);//定时发标
            }
            //招标截止时间
            br.setDisableDate(DateUtils.addDays(br.getPublishTime(),br.getDisableDays()));
        }else {
            //审核失败
            //借款状态修改为发标审核失败
            br.setBidRequestState(Constants.BIDREQUEST_STATE_PUBLISH_REFUSE);
            //移除用户是否有借款申请流程中的状态
            UserInfo userInfo = userInfoService.get(br.getCreateUser().getId());
            Long tempState = BitStatesUtils.removeState(userInfo.getBitState(), BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
            userInfo.setBitState(tempState);

            userInfoService.update(userInfo);
        }

        update(br);
    }

    public List<BidRequest> queryForLisr(BidRequestQueryObject qo) {
        return bidRequestMapper.queryForList(qo);
    }

    private void update(BidRequest br) {
        int count = bidRequestMapper.updateByPrimaryKey(br);
        if (count == 0){
            throw new DisplayableException("借款信息修改失败[乐观锁异常id:"+br.getId()+"]");
        }
    }

    /**
     * 每个一秒,调用该方法
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void bidRequestPublishCheck(){
        //查询待发布的借款
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setBidRequestState(Constants.BIDREQUEST_STATE_PUBLISH_PENDING);
        List<BidRequest> bidRequests = bidRequestMapper.queryForList(qo);

        Date now = new Date();
        for (int i = 0; i <bidRequests.size() ; i++) {
            BidRequest bidRequest = bidRequests.get(i);
            //判断发标时间是否满足
            if (now.after(bidRequest.getPublishTime())) {
                //执行发标
                bidRequest.setBidRequestState(Constants.BIDREQUEST_STATE_BIDDING);
                update(bidRequest);
            }
        }
    }

    public BidRequest get(Long id){
        return bidRequestMapper.selectByPrimaryKey(id);
    }


    public void bid(Long bidRequestId, BigDecimal amount) {
        //判断借款的状态必须处于招标中
        BidRequest br = bidRequestMapper.selectByPrimaryKey(bidRequestId);
        if (br == null) {
            throw new DisplayableException("无效的借款对象");
        }
        //借款人本人不能投标
        LoginInfo currentUser = UserContext.getLoginInfo();
        if (currentUser.getId().equals(br.getCreateUser().getId())) {
            throw new DisplayableException("不能投自己的借款");
        }
        //投标金额不能大于可用余额
        Account bidAccount = accountService.get(currentUser.getId());
        if (amount.compareTo(bidAccount.getUsableAmount()) > 0){
            throw new DisplayableException("您的可用余额不足!");
        }

        //投资金额不能大于剩余可投标金额
        if (amount.compareTo(br.getRemainAmount()) > 0){
            throw new DisplayableException("最大可投:"+br.getRemainAmount());
        }
        //如果借款的剩余可投标金额已经小于了最小投标金额
        if (br.getRemainAmount().compareTo(br.getMinBidAmount()) < 0) {
            // 允许一次把剩下的投完
            if (amount.compareTo(br.getRemainAmount()) != 0) {
                throw new DisplayableException("当前可投金额:"+br.getRemainAmount());
            }
        }else {
            //否则投资金额不能小于最小投标金额
            if (amount.compareTo(br.getMinBidAmount()) < 0){
                throw new DisplayableException("最小投资金额:"+br.getMinBidAmount());
            }
        }
        //一个投资人对于一个表总投资金额不能超过 借款金额的 50%
        BigDecimal bidTotalAmount = bidService.sumUserBidAmountByBidRequestId(br.getId(),currentUser.getId());
        bidTotalAmount = bidTotalAmount.add(amount);
        BigDecimal tempAmount = br.getBidRequestAmount().multiply(new BigDecimal("0.5"));
        if (bidTotalAmount.compareTo(tempAmount) > 0) {
            throw new DisplayableException("投标金额不能超过:"+tempAmount);
        }

        //执行投标
        //保存投标记录
        bidService.save(br,amount,currentUser);
        //投资人可用余额减少
        Account account = accountService.get(currentUser.getId());
        account.setUsableAmount(account.getUsableAmount().subtract(amount));
        //冻结金额增加
        account.setFreezedAmount(account.getFreezedAmount().add(amount));

        accountService.update(account);

        /**
         * 发送站内信息
         */
        LoginInfo loginInfo = UserContext.getLoginInfo();

        actionMessageService.actionMessage(loginInfo,"投标成功",
                "您已成功投标"+amount+"元",new Date());

        //参数冻结流水
        accountFlowService.createBidFlow(account,amount);
        //借款对象投标总数增加
        br.setBidCount(br.getBidCount() + 1);
        //投标总金额增加
        br.setCurrentSum(br.getCurrentSum().add(amount));
        //如果已经投满
        if (br.getCurrentSum().compareTo(br.getBidRequestAmount()) == 0){
            //借款对象状态修改为满标一审
            br.setBidRequestState(Constants.BIDREQUEST_STATE_APPROVE_PENDING_1);
            //该借款对象下面的投标对象的状态修改为满标一审
            bidService.batchUpdateState(br.getId(),Constants.BIDREQUEST_STATE_APPROVE_PENDING_1);
        }
        update(br);

    }


    public void audit1(Long id, int state, String remark) {
        //判断借款对象状态处于满标一审
        BidRequest br = bidRequestMapper.selectByPrimaryKey(id);//通过id获取借款对象信息
        if (br.getBidRequestState() != Constants.BIDREQUEST_STATE_APPROVE_PENDING_1) {
            throw new DisplayableException("借款状态不处于满标一审");
        }
        //设置审核相关信息
        //这个save是已经抽取好的方法
        bidRequestAuditHistoryService.save(br,BidRequestAuditHistory.TYPE_AUDIT1,remark,state);

        //审核成功
        if (state == BaseAuditDomain.STATE_SUCCESS) {
            //修改借款状态为满标二审
            br.setBidRequestState(Constants.BIDREQUEST_STATE_APPROVE_PENDING_2);
            //该借款下面的投标对象状态修改为满标二审
            bidService.batchUpdateState(br.getId(),Constants.BIDREQUEST_STATE_APPROVE_PENDING_2);
        }else {
            //审核失败
            //修改借款状态为审核失败
            fullAuditReject(br);
        }
            update(br);

        //移除用户是否有申请中的借款的状态
        UserInfo userInfo = userInfoService.get(br.getCreateUser().getId());
        Long tempState = BitStatesUtils.removeState(userInfo.getBitState(),BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
        userInfo.setBitState(tempState);
        userInfoService.update(userInfo);
    }

    private void fullAuditReject(BidRequest br) {
        //审核失败
        //修改借款状态为审核失败
        br.setBidRequestState(Constants.BIDREQUEST_STATE_REJECTED);

        //该借款下面的投标对象状态改为满标审核失败
        bidService.batchUpdateState(br.getId(),Constants.BIDREQUEST_STATE_REJECTED);

        //拿到该借款下的所有投标对象
        List<Bid> bids = br.getBids();
        //投资人账户缓存
        Map<Long,Account> bidAccounts = new HashMap<Long, Account>();

        for (int i = 0; i <bids.size() ; i++) {
            Bid bid = bids.get(i);
            //投资人id
            Long userId = bid.getBidUser().getId();
            Account bidAccount = bidAccounts.get(userId);
            if (bidAccount == null) {
                //投资人账户
                bidAccount = accountService.get(userId);

                bidAccounts.put(userId,bidAccount);
            }

            //投资人冻结金额减少
            bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));

            //投资人可用余额增加
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(bid.getAvailableAmount()));
            //产生解冻流水
            accountFlowService.createBiditErrorFlow(bidAccount,bid.getAvailableAmount());
        }
        //修改投资人账户
        Collection<Account> values = bidAccounts.values();
        for (Account value : values) {
            accountService.update(value);
        }
    }


    public void audit2(Long id, int state, String remark) {
        //判断借款的状态处于满标二审
        BidRequest br = bidRequestMapper.selectByPrimaryKey(id);
        if (br.getBidRequestState() != Constants.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            throw new DisplayableException("借款状态不处于满标二审");
        }
        //设置审核相关信息
        bidRequestAuditHistoryService.save(br,BidRequestAuditHistory.TYPE_AUDIT2,remark,state);

        //如果审核成功
        if (state == BaseAuditDomain.STATE_SUCCESS) {
            //借款对象状态修改为还款中
            br.setBidRequestState(Constants.BIDREQUEST_STATE_PAYING_BACK);
            //该借款下面的投标对象修改为还款中
            bidService.batchUpdateState(br.getId(),Constants.BIDREQUEST_STATE_PAYING_BACK);

            //借款人:
            Account borrowAccount = accountService.get(br.getCreateUser().getId());

            //借款人收到借款,可用余额增加
            borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().add(br.getBidRequestAmount()));

            //增加收款流水
            accountFlowService.createBorrowSuccessFlow(borrowAccount,br.getBidRequestAmount());

            //剩余授信额度减少
            borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().subtract(br.getBidRequestAmount()));

            //待还总额增加:借款金额+总利息
            borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount().add(br.getBidRequestAmount()).add(br.getTotalRewardAmount()));

            //移除借款人是否有借款申请状态
            UserInfo borrowUserInfo = userInfoService.get(br.getCreateUser().getId());
            Long tempState = BitStatesUtils.removeState(borrowUserInfo.getBitState(), BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
            borrowUserInfo.setBitState(tempState);

            //更新借款人状态
            userInfoService.update(borrowUserInfo);

            //借款手续费
            BigDecimal managementCharge = CalculatetUtil.calAccountManagementCharge(br.getBidRequestAmount());
            borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().subtract(managementCharge));

            //更新数据
            accountService.update(borrowAccount);

            //产生管理费支付流水
            accountFlowService.createManagementChargeFlow(borrowAccount,managementCharge);

            //投资人:
            List<Bid> bids = br.getBids();
            //投资人账户缓存
            Map<Long,Account> bidAccounts = new HashMap<Long, Account>();
            for (int i = 0; i < bids.size() ; i++) {
                Bid bid = bids.get(i);
                //投资人账号
                Long userId = bid.getBidUser().getId();
                Account bidAccount = bidAccounts.get(userId);
                if (bidAccount == null) {
                    bidAccount = accountService.get(userId);
                    bidAccounts.put(userId,bidAccount);
                }
                //投资人冻结金额减少
                bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));

                //产生解冻流水
                accountFlowService.createUnFreezedAmountFlow(bidAccount,bid.getAvailableAmount());
                //待收本金增加
                bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().add(bid.getAvailableAmount()));
                //计算利息
                BigDecimal unReceiveInterest = CalculatetUtil.calBidInterest(br.getBidRequestAmount(), br.getMonthes2Return(),
                        br.getCurrentRate(),br.getReturnType(), bid.getAvailableAmount());
                //待收利息增加
                bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().add(unReceiveInterest));
            }
            //更新数据
            Collection<Account> accounts = bidAccounts.values();
            for (Account acc : accounts) {
                accountService.update(acc);
            }

            //生成还款计划
            //生成收款计划
            paymentScheduleService.createPaymentSchedule(br);

            //平台账号
            SystemAccount systemAccount = systemAccountService.getCurrent();
            //收到借款人的借款管理费
            systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(managementCharge));
            //产生平台管理费流水
            systemAccountFlowService.createManagementChargeFlow(systemAccount,managementCharge);

            /**
             * 更新数据
             */
            systemAccountService.update(systemAccount);

            /**
             * 发送站内信息
             */
            actionMessageService.actionMessage(br.getCreateUser(),"审核成功",
                    "借款成功,放款金额"+br.getBidRequestAmount()+"请记得每月准时还款",new Date());

        }else {
            //如果审核失败:
            //同一审审核失败
            fullAuditReject(br);

            /**
             * 发送站内信息
             */
            actionMessageService.actionMessage(br.getCreateUser(),"审核失败",
                    "您的账户评分不足,请补充完整资料后再申请",new Date());

        }
        update(br);
    }

    public void returnMoney(Long id) {
        //判断还款计划的状态要处于待还款
        PaymentSchedule ps = paymentScheduleService.get(id);
        if (ps.getState() != Constants.PAYMENT_STATE_NORMAL) {
            throw new DisplayableException("还款计划状态不处于待还款");
        }
        //当前登录用户必须是借款人
        LoginInfo loginInfo = UserContext.getLoginInfo();
        if (!loginInfo.getId().equals(ps.getCreateUserId())) {
            throw new DisplayableException("兄弟,不能操作他人账号还款");
        }

        //判断还款人的可用余额是否足够
        Account returnAccount = accountService.get(loginInfo.getId());
        // A compareTo B  > 0   等价  A > B
        if (returnAccount.getUsableAmount().compareTo(ps.getTotalAmount()) < 0){
            throw new DisplayableException("账户余额不足,请充值后再操作");
        }

        //执行还款
        //还款人
        //减少可用余额
        returnAccount.setUsableAmount(returnAccount.getUsableAmount().subtract(ps.getTotalAmount()));
        //增加还款流水
        accountFlowService.creteReturnMoneyFlow(returnAccount,ps.getTotalAmount());
        //增加剩余授信额度
        returnAccount.setRemainBorrowLimit(returnAccount.getRemainBorrowLimit().add(ps.getPrincipal()));
        //待还总额减少
        returnAccount.setUnReturnAmount(returnAccount.getUnReturnAmount().subtract(ps.getTotalAmount()));

        //更新数据returnAccount对象数据
        accountService.update(returnAccount);

        //还款计划
        //修改还款时间
        Date now = new Date();
        ps.setPayDate(now);
        //修改还款状态为 : 已还
        ps.setState(Constants.PAYMENT_STATE_DONE);

        //更新数据
        paymentScheduleService.update(ps);

        List<PaymentScheduleDetail> details = ps.getDetails();
        //收款人账户缓存
        Map<Long,Account> bidAccounts = new HashMap<Long, Account>();

        //平台:
        SystemAccount systemAccount = systemAccountService.getCurrent();

        for (int i = 0; i <details.size() ; i++) {
            //收款计划
            PaymentScheduleDetail detail = details.get(i);
            //修改收款时间
            detail.setDeadLine(now);
            //修改收款计划
            paymentScheduleDetailService.update(detail);

            //收款人 投标人
            Long bidUserId = detail.getToLoginInfoId();
            Account bidAccount = bidAccounts.get(bidUserId);
            if (bidAccount == null) {
                bidAccount = accountService.get(bidUserId);
                bidAccounts.put(bidUserId,bidAccount);
            }

            //可用余额增加
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(detail.getTotalAmount()));
            //创建收款流水
            accountFlowService.createGetMoneyFlow(bidAccount,detail.getTotalAmount());
            //待收本金减少
            bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(detail.getPrincipal()));
            //待收利息减少
            bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(detail.getInterest()));
            //计算支付利息管理费
            BigDecimal interestManager = CalculatetUtil.calAccountManagementCharge(detail.getInterest());
            //利息管理费
            bidAccount.setUsableAmount(bidAccount.getUsableAmount().subtract(interestManager));

            //生成支付利息管理费流水
            accountFlowService.createInterestMangerFlow(bidAccount,interestManager);

            //收取利息管理费:可用余额增加
            systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(interestManager));

            //判断是否有推荐人推荐注册
            if (loginInfo.getRecommend() != null) {

                //计算出平台收取利息的10%作为奖励给推荐人 multiply这个方法是表示 *乘号
                BigDecimal userMendManager = interestManager.multiply(CalculatetUtil.INTEREST_MANAGER_CHARGE_RATE);
                //bidUserMend.setUsableAmount(bidUserMend.getUsableAmount().add(userMendManager));
                bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(userMendManager));
                //生成奖励金流水
                accountFlowService.createUserMendManagerFlow(bidAccount,userMendManager);

                //给推荐人支付推荐奖金,可用余额减少
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().subtract(userMendManager));
                //支付推荐人奖金流水
                systemAccountFlowService.createManagerFlow(systemAccount,userMendManager);
                //更新数据
//                accountService.update(bidAccount);
            }

            //产生利息管理费流水
            systemAccountFlowService.createInterestManagerFlow(systemAccount,interestManager);
        }

        //修改systemAccount
        systemAccountService.update(systemAccount);


        //修改收款人账户
        Collection<Account> accounts = bidAccounts.values();
        for (Account acc : accounts) {

            accountService.update(acc);
        }

        //如果已还清
        List<PaymentSchedule> pss = paymentScheduleService.listByBidRequestId(ps.getBidRequestId());
        boolean success = true;
        for (PaymentSchedule bidRequest : pss) {
            if (bidRequest.getState() != Constants.PAYMENT_STATE_DONE) {
                success = false;
                break;
            }

        }
        /*for (int i = 0; i <pss.size() ; i++) {
            BidRequest bidRequest = pss.get(i);
            BidRequest tempPs = pss.get(i);
        }*/
        if (success){
            //借款状态修改为已还清
            BidRequest bidRequest = bidRequestMapper.selectByPrimaryKey(ps.getBidRequestId());
            bidRequest.setBidRequestState(Constants.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
            //更新数据
            update(bidRequest);

            //此借款下面的投标对象状态修改为已还清
            bidService.batchUpdateState(bidRequest.getId(),Constants.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
        }
    }

    /**
     * 首页总借款人数
     * @return
     */
    public int totalCreateUser() {
        return bidRequestMapper.totalCreateUser();
    }

    /**
     * 借出贷款总金额
     * @return
     */
    public BigDecimal totalBidRequestAmount() {
        return bidRequestMapper.totalBidRequestAmount();
    }

    /**
     * 已赚取总收益
     * @return
     */
    public BigDecimal totalRewardAmount() {
        return bidRequestMapper.totalRewardAmount();
    }
}
