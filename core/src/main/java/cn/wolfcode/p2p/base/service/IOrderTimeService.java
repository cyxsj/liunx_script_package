package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.OrderTime;

import java.util.List;

public interface IOrderTimeService {

    /**
     * 查询所有时间段
     * @return
     */
    List<OrderTime> selectAll();

    OrderTime get(Long timeId);
}
