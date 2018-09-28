package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeQueryObject;
import java.util.List;

public interface RechargeOfflineMapper {

    int insert(RechargeOffline entity);

    RechargeOffline selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RechargeOffline entity);

    int queryForCount(RechargeQueryObject qo);

    List<RechargeOffline> queryForList(RechargeQueryObject qo);
}