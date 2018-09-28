package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
/**
 * 借款查询
 */
public class BidRequestQueryObject extends QueryObject {

    private int bidRequestState = -1;
    //多个状态查询
    private int[] bidRequestStates;
    //排查字段
    private String orderBy;
}
