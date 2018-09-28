package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.business.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {

    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;

    //设置审核相关信息
    public void save(BidRequest br,int auditType,String remark,int state){
        BidRequestAuditHistory history = new BidRequestAuditHistory();
        //history.setAuditType(BidRequestAuditHistory.TYPE_PUBLISH);//发标审核
        history.setAuditType(auditType);
        history.setBidRequestId(br.getId());//借款对象
        history.setApplier(br.getCreateUser());//申请人
        history.setApplyTime(br.getApplyTime());//申请时间
        history.setAuditor(UserContext.getLoginInfo());//审核人

        Date now = new Date();
        history.setAuditTime(now);
        history.setRemark(remark);
        history.setState(state);

        bidRequestAuditHistoryMapper.insert(history);
    }
}
