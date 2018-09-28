package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class IpLogQueryObject extends QueryObject{
    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    //设置默认状态
    private int state = -1;

    private int userType = -1;

    //用户名
    private String username;
}
