package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 平台的银行账号对象
 */
@Setter
@Getter
public class PlatformBankInfo extends BaseDomain {

    //银行名称
    private String bankName;

    //账号名称
    private String accountName;

    //账号
    private String accountNumber;

    //银行支行名
    private String bankForkName;

    public String getJsonString(){
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        map.put("bankName",bankName);
        map.put("accountName",accountName);
        map.put("accountNumber",accountNumber);
        map.put("bankForkName",bankForkName);

        return JSON.toJSONString(map);
    }

}