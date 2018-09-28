package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.Bid;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidMapper {

    int insert(Bid entity);

    Bid selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Bid entity);

    BigDecimal sumUserBidAmountByBidRequestId(@Param("bidRequestId") Long bidRequestId,@Param("userId") Long userId);

    void batchUpdateState(@Param("bidRequestId") Long bidRequestId,@Param("state") int state);

    int selectAll();
}