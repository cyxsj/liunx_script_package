package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class VideoAuthQueryObject extends QueryObject{

    private int state =-1;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String username;

    //审核客服
    private Long auditorId = -1L;
}
