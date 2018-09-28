package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter@Getter
//平台账户
public class SystemAccount extends BaseDomain{

    //版本
    private Integer version;
    //可用余额
    private BigDecimal usableAmount = Constants.ZERO;
    //冻结金额
    private BigDecimal freezedAmount = Constants.ZERO;

}