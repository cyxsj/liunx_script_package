package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface IRealAuthService {
    /**
     * 实名认证申请 realAuth
     * @param realAuth
     */
    void apply(RealAuth realAuth);

    /**
     * 根据id获取 realAuth
     * @param realAuthId
     * @return
     */
    RealAuth get(Long realAuthId);

    PageResult query(RealAuthQueryObject qo);

    /**
     * 实名认证审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
