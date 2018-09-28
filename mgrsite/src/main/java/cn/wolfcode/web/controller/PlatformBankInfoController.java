package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 平台银行卡账号管理
 */
@Controller
public class PlatformBankInfoController {

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;


    /**
     * 系统银行账号管理页面
     * @param model
     * @param qo
     * @return
     */
    @RequestMapping("companyBank_list")
    public String companyBankList(Model model,@ModelAttribute("qo") QueryObject qo){

        PageResult pageResult = platformBankInfoService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "platformbankinfo/list";
    }

    @RequestMapping("companyBank_update")
    @ResponseBody
    public JSONResult companyBankUpdate(PlatformBankInfo info){
        JSONResult jsonResult = new JSONResult();
        try {
            platformBankInfoService.saveOrUpdate(info);
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
