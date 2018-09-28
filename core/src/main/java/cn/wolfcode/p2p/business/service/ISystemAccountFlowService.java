package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.SystemAccount;

import java.math.BigDecimal;

public interface ISystemAccountFlowService {

    /**
     * 创建收取管理费流水
     */
    void createManagementChargeFlow(SystemAccount account, BigDecimal amount);

    /**
     * 收取利息管理费
     * @param systemAccount
     */
    void createInterestManagerFlow(SystemAccount systemAccount,BigDecimal interestManager);

    /**
     * 给推荐人支付推荐奖金
     * @param systemAccount
     * @param interestManager
     */
    void createManagerFlow(SystemAccount systemAccount, BigDecimal interestManager);
}
