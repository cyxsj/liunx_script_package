package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.DigestException;

/**
 * 注册相关
 */
@Controller
public class RegisterController {

    @Autowired
    private ILoginInfoService loginInfoService;
    /**
     * 提交注册
     */
    @RequestMapping("userRegister")
    @ResponseBody
    public JSONResult userRegister(String username,String verifycode,String password,String confirmPwd,Long recommend){
        JSONResult jsonResult = new JSONResult();
        try {
            loginInfoService.regidter(username,verifycode,password,confirmPwd,recommend);
        }catch (DisplayableException e){
            e.printStackTrace();
            jsonResult.mark(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.mark("系统萎了,我们正在殴打老杨");
        }
        return jsonResult;
    }

    /**
     * 判断用户是否真实存在
     */
    @RequestMapping("existUsername")
    @ResponseBody
    public boolean existUsername(String username){
        return !loginInfoService.existUsername(username);
    }

}
