package cn.wolfcode.controller;


import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 个人中心页面
 */
@Controller
public class PersonalController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @Autowired
    private ILoginInfoService loginInfoService;


    @RequestMapping("personal")
    @NeedLogin
    public String list(Model model){
        //获取当前登录用户对象
        Long userId = UserContext.getLoginInfo().getId();
        model.addAttribute("account",accountService.get(userId));

        /**
         * 注册推荐码
         */
        model.addAttribute("loginInfo",loginInfoService.selectById(userId));
        return "personal";
    }

    /**
     * 推荐码注册
     */
    @RequestMapping("register_code")
    public String recommend(@ModelAttribute("recommend") Long id){
        return "register";
    }


    /**
     * 填写个人资料页面
     * @return
     */
    @RequestMapping("basicInfo")
    public String basicInfo(Model model){
        LoginInfo loginInfo = UserContext.getLoginInfo();
        List<SystemDictionaryItem> educationBackgrounds = systemDictionaryService.listItemBySn("educationBackgrounds");
        List<SystemDictionaryItem> incomeGrades = systemDictionaryService.listItemBySn("incomeGrades");
        List<SystemDictionaryItem> marriages = systemDictionaryService.listItemBySn("marriages");
        List<SystemDictionaryItem> kidCounts = systemDictionaryService.listItemBySn("kidCounts");
        List<SystemDictionaryItem> houseConditions = systemDictionaryService.listItemBySn("houseConditions");

        model.addAttribute("educationBackgrounds",educationBackgrounds);
        model.addAttribute("incomeGrades",incomeGrades);
        model.addAttribute("marriages",marriages);
        model.addAttribute("kidCounts",kidCounts);
        model.addAttribute("houseConditions",houseConditions);

        UserInfo userInfo = userInfoService.get(loginInfo.getId());
        model.addAttribute("userinfo",userInfo);
        return "userInfo";
    }

    /**
     * 保存基本资料
     */
    @RequestMapping("basicInfo_save")
    @ResponseBody
    public JSONResult basicInfoSave(UserInfo userInfo){
        JSONResult jsonResult = new JSONResult();
        try {
            userInfoService.basicInfoSave(userInfo);
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
