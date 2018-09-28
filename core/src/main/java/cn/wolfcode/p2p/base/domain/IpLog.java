package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
/**
 * 登录日志
 */
public class IpLog extends BaseDomain{

    /**
     * 登录状态:成功
     */
    public static final int STATE_NORMAL = 0;
    /**
     * 登录状态:失败
     */
    public static final int STATE_ERROR = 1;

    /**
     * 用户类型:前台
     */
    public static final int USERTYPE_WEBSITE = 0;
    /**
     * 用户类型:后台
     */
    public static final int USERTYPE_MGRSITE = 1;


    //ip
    private String ip;

    //登录时间
    private Date loginTime;

    //登录用户名
    private String username;

    //登录状态
    private int state = STATE_ERROR;

    //登录类型,前台 后台
    private int userType = USERTYPE_WEBSITE;


    public String getStateName(){
        return state == STATE_NORMAL ? "登录成功":"登录失败";
    }


    public String getStateType(){
        return userType == USERTYPE_WEBSITE ? "前台用户":"后台用户";
    }

}