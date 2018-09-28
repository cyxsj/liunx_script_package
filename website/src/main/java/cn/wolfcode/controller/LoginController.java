package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.anno.NotNeedLogin;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.UserContext;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.acl.LastOwnerException;

@Controller
public class LoginController {

    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private IIpLogService iIpLogService;

    @ResponseBody
    @RequestMapping("userLogin")
    public JSONResult userLogin(String username, String password, HttpServletRequest request){
        JSONResult jsonResult = new JSONResult();
        //默认登录失败
        int state = IpLog.STATE_ERROR;
        try {
            loginInfoService.login(username,password, false);
            //登录成功
            state = IpLog.STATE_NORMAL;
        }catch (DisplayableException e){
            e.printStackTrace();
            jsonResult.mark(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.mark("系统异常,我们正在殴打老杨");
        }
        //保存登录日志
        iIpLogService.save(IpLog.USERTYPE_WEBSITE,username, request.getRemoteAddr(), state);
        return jsonResult;
    }

    /**
     * 注销登录用户
     */
    @RequestMapping("logout")
    public String logout(){
        UserContext.getSession().removeAttribute(UserContext.LOGININFO_IN_SESSION);
        return "redirect:/login.html";
    }
}
