package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.domain.BidRequest;

import java.math.BigDecimal;
import java.util.List;

public interface IBidService {
    /**
     * 查询用户针对一个接口的投标总额
     * @param bidRequestId
     * @param userId
     * @return
     */
    BigDecimal sumUserBidAmountByBidRequestId(Long bidRequestId,Long userId);

    /**
     * 保存一个投标对象
     * @param br
     * @param amount
     * @param currentUser
     */
    void save(BidRequest br, BigDecimal amount, LoginInfo currentUser);

    /**
     * 根据借款对象id修改投标对象的状态
     * @param id
     * @param bidrequestStateApprovePending1
     */
    void batchUpdateState(Long id, int bidrequestStateApprovePending1);

    /**
     * 修改投标账户
     * @param bidAccount
     */
    void update(Account bidAccount);

    /**
     * 查询所有投资人
     * @return
     */
    int selectAll();

    /**
     * 根据推荐码匹配推荐人
     * @param recommend
     * @return
     */
    Bid get(Long recommend);
}
