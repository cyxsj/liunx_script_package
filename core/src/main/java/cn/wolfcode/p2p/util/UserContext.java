package cn.wolfcode.p2p.util;


import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.vo.VerifyCodeVo;
import javafx.geometry.VPos;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class UserContext {

    public static final String VERIFYCODE_IN_SESSION = "verifycode_in_session";

    public static final String LOGININFO_IN_SESSION = "logininfo";

    //获取session ~ RequestContextHolder记住
    public static HttpSession getSession(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession();
        return session;
    }

    /**
     * 设置验证码发送记录到session中
     * @param vo
     */
    public static void setVerifyCode(VerifyCodeVo vo){
        //ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //HttpSession session = requestAttributes.getRequest().getSession();
        getSession().setAttribute(VERIFYCODE_IN_SESSION,vo);
    }

    /**
     * 从session中获取验证码发送记录
     */
    public static VerifyCodeVo getVerifyCode(){
        return (VerifyCodeVo)getSession().getAttribute(VERIFYCODE_IN_SESSION);
    }


    /**
     * 把logininof放到session中
     * @param logininfo
     */
    public static void setLoginInfo(LoginInfo logininfo){
        getSession().setAttribute(LOGININFO_IN_SESSION,logininfo);
    }

    /**
     * 从session中获取logininfo
     */
    public static LoginInfo getLoginInfo(){
        return (LoginInfo) getSession().getAttribute(LOGININFO_IN_SESSION);
    }
}
