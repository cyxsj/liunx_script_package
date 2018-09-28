package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RealAuth extends BaseAuditDomain{

    //性别 男
    public static final int SEX_MAN = 0;
    //性别 女
    public static final int SEX_WOMAN = 1;




    public String getJsonString(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("realname",realName);
        map.put("idNumber",idNumber);
        map.put("sex",getSexDisplay());
        //会根据系统时间格式进行区分
        String format = DateFormat.getDateInstance().format(bornDate);
        map.put("bornDate",format);
        map.put("address",address);
        map.put("image1",image1);
        map.put("image2",image2);
        return JSON.toJSONString(map);
    }

    public String getSexDisplay(){
        return sex == SEX_MAN ? "男":"女";
    }


    private String realName;
    private String idNumber;

    private Byte sex;
    //出生日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bornDate;
    private String address;
    private String image1;
    private String image2;

}