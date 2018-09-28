package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台主页
 */
@Controller
public class IpLoginController {
    @Autowired
    private IIpLogService iIpLogService;
    @RequestMapping("ipLog")
    public String ipLog(Model model,IpLogQueryObject qo){
        PageResult pageResult = iIpLogService.query(qo);
        model.addAttribute("qo",qo);
        model.addAttribute("pageResult",pageResult);
        return "ipLog/list";
    }
}
