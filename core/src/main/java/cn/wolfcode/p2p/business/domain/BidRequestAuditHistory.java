package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
/**
 * 借款审核记录
 */
public class BidRequestAuditHistory extends BaseAuditDomain{

    //发标审核
    public static final int TYPE_PUBLISH = 0;
    //满标一审
    public static final int TYPE_AUDIT1 = 1;
    //满标二审
    public static final int TYPE_AUDIT2  = 2;


    //借款对象
    private Long bidRequestId;
    //审核类型
    private int auditType;

}