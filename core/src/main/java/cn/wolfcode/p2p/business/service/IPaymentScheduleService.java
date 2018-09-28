package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.List;

public interface IPaymentScheduleService {
    /**
     * 生成还款计划
     */
    void createPaymentSchedule(BidRequest br);

    /**
     * 分页查询还款页面
     * @param qo
     * @return
     */
    PageResult query(PaymentScheduleQueryObject qo);

    /**
     * 根据id获取还款计划对象
     * @param id
     * @return
     */
    PaymentSchedule get(Long id);

    /**
     * 根据借款id查询所有的还款计划列表
     * @param bidRequestId
     * @return
     */
    List<PaymentSchedule> listByBidRequestId(Long bidRequestId);

    /**
     *
     * @param ps
     */
    void update(PaymentSchedule ps);
}
