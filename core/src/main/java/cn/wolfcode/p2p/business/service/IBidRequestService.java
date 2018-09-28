package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IBidRequestService {
    /**
     * 借款申请
     * @param br
     */
    void apply(BidRequest br);

    /**
     * 借款分页查询
     * @param qo
     * @return
     */
    PageResult query(BidRequestQueryObject qo);

    /**
     * 发标审核
     * @param id
     * @param remark
     * @param state
     * @param publishTime
     */
    void publishAudit(Long id, String remark, int state, Date publishTime);

    /**
     * 根据条件查询借款列表
     * @param qo
     * @return
     */
    List<BidRequest> queryForLisr(BidRequestQueryObject qo);

    /**
     * 发标检查
     */
    void bidRequestPublishCheck();

    /**
     * 根据id获取借款对象
     * @param id
     * @return
     */
    BidRequest get(Long id);

    /**
     * 执行投标
     * @param bidRequestId
     * @param amount
     */
    void bid(Long bidRequestId, BigDecimal amount);

    /**
     * 满标一审
     * @param id
     * @param state
     * @param remark
     */
    void audit1(Long id, int state, String remark);

    /**
     * 满标二审
     * @param id
     * @param state
     * @param remark
     */
    void audit2(Long id, int state, String remark);

    /**
     * 借款人执行还款
     * @param id
     */
    void returnMoney(Long id);

    /**
     * 总借款人数
     * @return
     */
    int totalCreateUser();

    /**
     * 借出 总贷款金额
     * @return
     */
    BigDecimal totalBidRequestAmount();

    /**
     * 已赚取总收益
     * @return
     */
    BigDecimal totalRewardAmount();
}
