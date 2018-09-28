package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.List;

public interface IRechargeOfflineService {
    /**
     * 线下充值审核
     * @param recharge
     */
    void apply(RechargeOffline recharge);

    /**
     *  分页查询
     * @param qo
     * @return
     */
    PageResult query(RechargeQueryObject qo);

    /**
     * 线下充值审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
