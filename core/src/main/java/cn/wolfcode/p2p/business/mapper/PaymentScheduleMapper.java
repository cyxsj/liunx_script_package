package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import java.util.List;

public interface PaymentScheduleMapper {

    int insert(PaymentSchedule entity);

    PaymentSchedule selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PaymentSchedule entity);

    int queryForCount(PaymentScheduleQueryObject qo);

    List<PaymentSchedule> queryForList(PaymentScheduleQueryObject qo);

    List<PaymentSchedule> listByBidRequestId(Long bidRequestId);
}