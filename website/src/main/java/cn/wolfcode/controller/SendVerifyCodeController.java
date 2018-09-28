package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.ISendVerifyCodeService;
import cn.wolfcode.p2p.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendVerifyCodeController {

    @Autowired
    private ISendVerifyCodeService sendVerifyCodeService;

    @ResponseBody
    @RequestMapping("sendVerifyCode")
    public JSONResult sendVerifyCode(String phoneNumber){
        JSONResult jsonResult = new JSONResult();
        try {
            sendVerifyCodeService.send(phoneNumber);
        }catch (DisplayableException e){
            e.printStackTrace();
            jsonResult.mark(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.mark("系统异常啦!我们正在殴打 老杨(系统开发负责人)");
        }
        return jsonResult;
    }
}
