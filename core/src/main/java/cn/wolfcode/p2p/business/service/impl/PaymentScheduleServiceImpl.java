package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleDetailMapper;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleMapper;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import cn.wolfcode.p2p.util.CalculatetUtil;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.DateUtil;
import cn.wolfcode.p2p.util.PageResult;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;


    public void createPaymentSchedule(BidRequest br) {
        Date beginDate = br.getPublishTime();
        //借款期限:Monthes2Return
        for (int i = 0; i < br.getMonthes2Return(); i++) {
            PaymentSchedule ps = new PaymentSchedule();
            ps.setBidRequestId(br.getId());
            ps.setBidRequestTitle(br.getTitle());
            ps.setBidRequestType(br.getBidRequestType());
            ps.setCreateUserId(br.getCreateUser().getId());

            //还款截止时间
            int monthIndex = i+1;
            ps.setDeadLine(DateUtils.addMonths(beginDate,monthIndex));
            ps.setMonthIndex(monthIndex);
            ps.setReturnType(br.getReturnType());
            ps.setState(Constants.PAYMENT_STATE_NORMAL);

            //本期还款利息
            BigDecimal interest = CalculatetUtil.calMonthlyInterest(br.getReturnType(),
                    br.getBidRequestAmount(), br.getCurrentRate(), monthIndex, br.getMonthes2Return());
            ps.setInterest(interest.setScale(Constants.SCALE_STORE,RoundingMode.HALF_UP));

            //计算还款总额
            BigDecimal totalAmount = CalculatetUtil.calMonthToReturnMoney(br.getReturnType(), br.getBidRequestAmount(),
                    br.getCurrentRate(), monthIndex, br.getMonthes2Return());
            //设置还款总额
            ps.setTotalAmount(totalAmount);
            //本期还款本金
            ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));

            paymentScheduleMapper.insert(ps);

            createDetail(ps,br);
        }
    }

    /**
     * 还款分页
     * @param qo
     * @return
     */
    public PageResult query(PaymentScheduleQueryObject qo) {
        int count = paymentScheduleMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }
        List<PaymentSchedule> list = paymentScheduleMapper.queryForList(qo);
        return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
    }

    /**
     * 还款对象
     * @param id
     * @return
     */
    public PaymentSchedule get(Long id) {
        return paymentScheduleMapper.selectByPrimaryKey(id);
    }

    /**
     * 还款计划列表
     */
    public List<PaymentSchedule> listByBidRequestId(Long bidRequestId) {
        return paymentScheduleMapper.listByBidRequestId(bidRequestId);
    }

    public void update(PaymentSchedule ps) {
        int count = paymentScheduleMapper.updateByPrimaryKey(ps);
        if (count == 0) {
            throw new DisplayableException("还款计划修改失败");
        }
    }

    private void createDetail(PaymentSchedule ps, BidRequest br) {
        List<Bid> bids = br.getBids();
        for (int i = 0; i < bids.size(); i++) {
            Bid bid = bids.get(i);

            PaymentScheduleDetail detail = new PaymentScheduleDetail();
            detail.setBidAmount(bid.getAvailableAmount());
            detail.setBidId(bid.getId());
            detail.setBidRequestId(br.getId());
            detail.setDeadLine(ps.getDeadLine());
            detail.setFromLoginInfo(br.getCreateUser());
            detail.setMonthIndex(ps.getMonthIndex());
            detail.setPaymentScheduleId(ps.getId());
            detail.setReturnType(br.getReturnType());
            detail.setToLoginInfoId(bid.getBidUser().getId());

            //收款本金 = bid投标金额/总借款金额 * 还款计划的本期金额
            BigDecimal scale = bid.getAvailableAmount().divide(br.getBidRequestAmount(),Constants.SCALE_CAL,RoundingMode.HALF_UP);

            detail.setPrincipal(scale.multiply(ps.getPrincipal()).setScale(Constants.SCALE_STORE,RoundingMode.HALF_UP));
            detail.setInterest(scale.multiply(ps.getInterest()).setScale(Constants.SCALE_STORE,RoundingMode.HALF_UP));
            detail.setTotalAmount(detail.getPrincipal().add(detail.getInterest()));

            paymentScheduleDetailMapper.insert(detail);
        }
    }

}
