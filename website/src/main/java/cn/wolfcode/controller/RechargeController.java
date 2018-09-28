package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 充值相关
 */
@Controller
public class RechargeController {

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    @RequestMapping("recharge")
    public String recharge(Model model){
        model.addAttribute("banks",platformBankInfoService.selectAll());
        return "recharge";
    }

    /**
     * 线下充值申请
     * @return
     */
    @RequestMapping("recharge_save")
    @ResponseBody
    public JSONResult rechargeSave(RechargeOffline recharge){
        JSONResult jsonResult = new JSONResult();
        try {
            rechargeOfflineService.apply(recharge);
        }catch (DisplayableException e){
             e.printStackTrace();
             jsonResult.mark(e.getMessage());
        }catch (Exception e){
             e.printStackTrace();
             jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }

}
