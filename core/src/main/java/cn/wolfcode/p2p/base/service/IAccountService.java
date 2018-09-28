package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Account;

import java.math.BigDecimal;

public interface IAccountService {
    /**
     * 初始化
     * @param id
     */
    void init(Long id);

    /**
     * 根据id获取account对象
     * @param userId
     * @return
     */
    Account get(Long userId);

    /**
     * 修改账户
     * @param account
     */
    void update(Account account);

    /**
     * 总待收本息
     * @return
     */
    BigDecimal totalUnReceiveInterest();
}
