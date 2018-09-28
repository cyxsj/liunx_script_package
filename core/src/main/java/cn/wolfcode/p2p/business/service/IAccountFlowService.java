package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.Bid;

import java.math.BigDecimal;

public interface IAccountFlowService {
    /**
     * 创建线下充值流水
     * @param account
     * @param amount
     */
    void createRechargeOfflineFlow(Account account, BigDecimal amount);

    /**
     * 创建投标流水
     */
    void createBidFlow(Account account, BigDecimal amount);

    /**
     * 一审失败,解冻流水
     * @param bidAccount
     * @param availableAmount
     */
    void createBiditErrorFlow(Account bidAccount, BigDecimal availableAmount);

    /**
     * 借款成功流水
     */
    void createBorrowSuccessFlow(Account account, BigDecimal amount);

    /**
     * 创建借款管理费流水
     */
    void createManagementChargeFlow(Account account,BigDecimal amount);

    /**
     * 投资人投标成功,产生解冻流水
     * @param bidAccount
     * @param availableAmount
     */
    void createUnFreezedAmountFlow(Account bidAccount, BigDecimal availableAmount);

    /**
     * 增加/创建还款流水
     */
    void creteReturnMoneyFlow(Account account,BigDecimal amount);

    /**
     * 创建收款流水
     * @param bidAccount
     * @param amount
     */
    void createGetMoneyFlow(Account bidAccount, BigDecimal amount);

    /**
     * 支付利息管理费流水
     * @param bidAccount
     * @param amount
     */
    void createInterestMangerFlow(Account bidAccount, BigDecimal amount);

    /**
     * 生成奖励金
     * @param bidUserMend
     * @param userMendManager
     */
    void createUserMendManagerFlow(Account bidUserMend, BigDecimal userMendManager);
}
