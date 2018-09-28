package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 站内消息
 */
@Setter
@Getter
public class ActionMessage extends BaseDomain{
    //信息标题
    private String title;
    //信息内容
    private String context;
    //借款人/投资人
    private LoginInfo receiver;

    //信息发送时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noteTime;

    //状态：借款/投资 成功/失败
    private int state = NORMAL_SITUATION;
    //已读/未读
    private boolean readState = false;

    public static final int NORMAL_SITUATION = 0;//正常情况
    public static final int BIDREQUEST_SUCCESS = 1;//借款成功
    public static final int BIDREQUEST_FAILURE = 2;//借款失败
    public static final int BID_SUCCESS = 3;//投资成功


    public String getReadStateDisplay(){
        return readState == false ? "未读":"已读";
    }

    public String getStateDisplay(){
        switch (state){
            case NORMAL_SITUATION : return "正常情况";
            case BIDREQUEST_SUCCESS : return "借款成功";
            case BIDREQUEST_FAILURE : return "借款失败";
            case BID_SUCCESS : return "投资成功";
        }
        return "未知";
    }
}