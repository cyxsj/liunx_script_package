package cn.wolfcode.p2p.base.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户登录
 */
@Setter
@Getter
public class LoginInfo extends BaseDomain{
    //用户状态:正常
    public static final int STATE_NORMAL = 0;
    //用户状态:锁定
    public static final int STATE_LOCK = 1;


    //用户类型:前台用户
    public static final int USERTYPE_WEBSITE = 0;
    //用户类型:后台用户
    public static final int USERTYPE_MGRSITE = 1;
    //用户类型:后台审核人员
    public static final int USERTYPE_AUDITOR = 2;

    //用户名
    private String username;
    //密码
    private String password;
    //用户状态
    private int state = STATE_NORMAL;

    /**
     * 老用户推荐码
     */
    private Long recommend;


    //用户类型
    private int userType = USERTYPE_WEBSITE;

    public Boolean isMgrSiteUser(){
        switch (userType){
            case USERTYPE_WEBSITE:return false;
            case USERTYPE_MGRSITE:return true;
            case USERTYPE_AUDITOR:return true;
        }
        return null;
    }
}
