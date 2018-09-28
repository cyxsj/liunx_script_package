package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.anno.NotNeedLogin;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
/**
 * 后台登录相关
 */
public class LoginController {

    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private IIpLogService iIpLogService;

    @RequestMapping("userLogin")
    @ResponseBody
    @NotNeedLogin
    public JSONResult userLogin(String username, String password, HttpServletRequest request){
        JSONResult jsonResult = new JSONResult();
        //默认登录失败
        int state = IpLog.STATE_ERROR;
        try {
            loginInfoService.login(username,password, true);
            state = IpLog.STATE_NORMAL;
        }catch (DisplayableException e){
            e.printStackTrace();
            jsonResult.mark(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.mark("系统异常,我们正在殴打老杨");
        }
        //保存日志
        iIpLogService.save(IpLog.USERTYPE_MGRSITE,username,request.getRemoteAddr(),state);
        return jsonResult;
    }
}
