package cn.wolfcode.p2p.base.domain;

import cn.wolfcode.p2p.util.Constants;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
/**
 * 用户账户信息
 */
public class Account extends BaseDomain{
    //版本
    private int version = 0;
    //交易密码
    private String tradePassword;
    //可用余额
    private BigDecimal usableAmount = Constants.ZERO;
    //冻结金额
    private BigDecimal freezedAmount = Constants.ZERO;
    //待收利息
    private BigDecimal unReceiveInterest = Constants.ZERO;
    //待收本金
    private BigDecimal unReceivePrincipal = Constants.ZERO;
    //待还总额
    private BigDecimal unReturnAmount = Constants.ZERO;
    //剩余授信额度
    private BigDecimal remainBorrowLimit = Constants.BORROW_LIMIT;
    //授信额度
    private BigDecimal borrowLimitAmount = Constants.BORROW_LIMIT;

    //计算账户总额=   可用余额+冻结金额+待收本金
    public BigDecimal getTotalAmount(){
        return this.getUsableAmount().add(this.getFreezedAmount().add(this.getUnReceivePrincipal()));
    }

}