package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IpLogController {
    /**
     * 登录日志相关
     */
    @Autowired
    private IIpLogService ipLogService;


    @RequestMapping("ipLog")
    @NeedLogin
    public String ipLog(@ModelAttribute("qo") IpLogQueryObject qo, Model model){

        qo.setUsername(UserContext.getLoginInfo().getUsername());
        PageResult result = ipLogService.query(qo);
//        model.addAttribute("qo",qo);
        model.addAttribute("pageResult",result);
        return "iplog_list";
    }
}
