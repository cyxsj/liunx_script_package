package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
/**
 * 视频认证对象
 */
public class VideoAuth extends BaseAuditDomain {

    //预约开始时间
    private Date orderBeginDate;
    //预约结束时间
    private Date orderEndDate;

    public String getJsonString(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("orderTime", orderBeginDate.toLocaleString()
                +" - "+ orderEndDate.toLocaleString());
        return JSON.toJSONString(map);
    }

}
