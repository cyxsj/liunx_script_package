package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.business.query.RechargeQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 充值审核相关
 */
@Controller
public class RechargeController {

    @Autowired
    private IRechargeOfflineService rechargeOfflineService;
    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    /**
     * 线下充值审核页面
     */
    @RequestMapping("rechargeOffline")
    public String rechargeOffline(Model model,@ModelAttribute("qo") RechargeQueryObject qo){

        PageResult pageResult = rechargeOfflineService.query(qo);
        model.addAttribute("pageResult",pageResult);

        model.addAttribute("banks",platformBankInfoService.selectAll());
        return "rechargeoffline/list";
    }

    /**
     * 线下充值审核
     */

    @RequestMapping("rechargeOffline_audit")
    @ResponseBody
    public JSONResult rechargeOfflineAudit(Long id,int state,String remark){
        JSONResult jsonResult = new JSONResult();
        try {
            rechargeOfflineService.audit(id,state,remark);
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
