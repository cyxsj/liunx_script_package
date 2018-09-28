package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.OrderTime;
import java.util.List;

public interface OrderTimeMapper {

    int insert(OrderTime entity);

    OrderTime selectByPrimaryKey(Long id);

    List<OrderTime> selectAll();

    int updateByPrimaryKey(OrderTime entity);
}