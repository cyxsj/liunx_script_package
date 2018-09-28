package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;

import java.math.BigDecimal;
import java.util.List;

public interface BidRequestMapper {

    int insert(BidRequest entity);

    BidRequest selectByPrimaryKey(Long id);


    int updateByPrimaryKey(BidRequest entity);

    int queryForCount(BidRequestQueryObject qo);

    List<BidRequest> queryForList(BidRequestQueryObject qo);

    int totalCreateUser();

    BigDecimal totalBidRequestAmount();

    BigDecimal totalRewardAmount();
}