package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 线下充值
 */
@Setter
@Getter
public class RechargeOffline extends BaseAuditDomain{

    //充值的平台账号
    private PlatformBankInfo bankInfo;
    //充值流水号
    private String tradeCode;
    //交易时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tradeTime;
    //充值金额
    private BigDecimal amount;
    //备注
    private String note;

    public String getJsonString(){

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("tradeCode",tradeCode);
        map.put("amount",amount);
        map.put("tradeTime", DateFormat.getDateInstance().format(tradeTime));
        return JSON.toJSONString(map);
    }



}