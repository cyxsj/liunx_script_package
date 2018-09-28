package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
/**
 * 账户流水对象
 */
public class AccountFlow extends BaseDomain{

    //交易类型
    private int actionType;
    //交易金额
    private BigDecimal amount;
    //交易说明
    private String note;
    //可用余额
    private BigDecimal usableAmount;
    //冻结金额
    private BigDecimal freezedAmount;
    //交易时间
    private Date actionTime;
    //交易账户
    private Long accountId;

}