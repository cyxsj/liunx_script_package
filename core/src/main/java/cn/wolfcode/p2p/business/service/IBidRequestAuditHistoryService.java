package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;

public interface IBidRequestAuditHistoryService {
    /**
     * @param br
     * @param auditType
     * @param remark
     * @param state
     */
    void save(BidRequest br, int auditType, String remark, int state);
}
