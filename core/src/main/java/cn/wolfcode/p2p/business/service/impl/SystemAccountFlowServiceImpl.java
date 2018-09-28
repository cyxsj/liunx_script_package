package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.domain.SystemAccountFlow;
import cn.wolfcode.p2p.business.mapper.SystemAccountFlowMapper;
import cn.wolfcode.p2p.business.service.ISystemAccountFlowService;
import cn.wolfcode.p2p.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {

    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;


    public void createBaseFlow(SystemAccount account, int actionType, BigDecimal amount, String note) {
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setActionTime(new Date());
        flow.setActionType(actionType);
        flow.setAmount(amount);
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setUsableAmount(account.getUsableAmount());
        flow.setNote(note);
        systemAccountFlowMapper.insert(flow);
    }

    public void createManagementChargeFlow(SystemAccount account,BigDecimal amount){
        createBaseFlow(account, Constants.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE,
                amount,"收取借款管理费:"+amount+"元 :"+new Date().toLocaleString());
    }
    public void createInterestManagerFlow(SystemAccount account,BigDecimal amount){
        createBaseFlow(account, Constants.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE,
                amount,"收取借款管理费:"+amount+"元 :"+new Date().toLocaleString());
    }

    public void createManagerFlow(SystemAccount account,BigDecimal amount){
        createBaseFlow(account, Constants.SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CODE,
                amount,"给推荐人支付奖励金:"+amount+"元 :"+new Date().toLocaleString());
    }

}
