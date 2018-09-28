package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.mapper.AccountFlowMapper;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Controller
@Transactional
public class AccountFlowServiceImpl implements IAccountFlowService {

    @Autowired
    private AccountFlowMapper accountFlowMapper;

    public void createBaseFlow(Account account, int actionType, BigDecimal amount,String note) {
        AccountFlow flow = new AccountFlow();
        flow.setAccountId(account.getId());
        Date now = new Date();
        flow.setActionTime(now);
        flow.setActionType(actionType);
        flow.setAmount(amount);
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setUsableAmount(account.getUsableAmount());
        flow.setNote(note);
        accountFlowMapper.insert(flow);
    }

    public void createRechargeOfflineFlow(Account account, BigDecimal amount){
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,
                amount,"线下充值成功:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createBidFlow(Account account, BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_BID_FREEZED,
                amount,"投标冻结:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createBiditErrorFlow(Account account, BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,
                amount,"满标审核失败,解冻:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createBorrowSuccessFlow(Account account, BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL,
                amount,"借款成功,可用余额增加:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createManagementChargeFlow(Account account,BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_CHARGE,
                amount,"借款成功,支付管理费:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createUnFreezedAmountFlow(Account account,BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL,
                amount,"投标成功,冻结金额减少:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void creteReturnMoneyFlow(Account account,BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_RETURN_MONEY,
                amount,"还款成功,可用余额减少:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createGetMoneyFlow(Account account,BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY,
                amount,"收款成功,可用余额增加:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createInterestMangerFlow(Account account,BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_INTEREST_SHARE,
                amount,"收款成功,支付平台利息管理费:"+amount+"元 :"+new Date().toLocaleString());
    }

    //收取平台奖励金
    public void createUserMendManagerFlow(Account account,BigDecimal amount) {
        createBaseFlow(account, Constants.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY,
                amount,"收款成功,收取平台推荐人奖励金:"+amount+"元 :"+new Date().toLocaleString());
    }
}
