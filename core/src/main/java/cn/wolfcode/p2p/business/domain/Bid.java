package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
/**
 * 一次投标对象
 */
public class Bid extends BaseDomain {

    private BigDecimal actualRate;
    private BigDecimal availableAmount;
    private Long bidRequestId;
    private String bidRequestTitle;
    //投资人
    private LoginInfo bidUser;
    private Date bidTime;
    private int bidRequestState;
}
