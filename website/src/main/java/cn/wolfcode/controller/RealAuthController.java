package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.UploadUtil;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 实名认证相关
 */
@Controller
public class RealAuthController {

    @Autowired
    private IRealAuthService iRealAuthService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IRealAuthService realAuthService;

    /**
     * 实名认证申请页面
     * @return
     */
    @RequestMapping("realAuth")
    public String realAuth(Model model){
        //判断用户是否完成实名认证->跳到成功页面
        Long userId = UserContext.getLoginInfo().getId();
        UserInfo userInfo = userInfoService.get(userId);
        if(userInfo.isRealAuth()){
            RealAuth realAuth = realAuthService.get(userInfo.getRealAuthId());
            model.addAttribute("realAuth",realAuth);
            return "realAuth_result";
        }

        //判断用户是否有实名申请->跳转等待页面
        if (userInfo.getRealAuthId() != null) {
            model.addAttribute("auditing",true);
            return "realAuth_result";
        }

        return "realAuth";
    }


    /**
     * 实名保存信息
     */
    @RequestMapping("realAuth_save")
    @ResponseBody
    public JSONResult realAuthSave(RealAuth realAuth){
        JSONResult jsonResult = new JSONResult();
        try {
            iRealAuthService.apply(realAuth);
        }catch (DisplayableException e){
             e.printStackTrace();
             jsonResult.mark(e.getMessage());
        }catch (Exception e){
             e.printStackTrace();
             jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }

    //文件上传目录
    @Value("${uploadpath}")
    private String uploadpath;
    /**
     * 图片上传
     */
    @RequestMapping("uploadPhoto")
    @ResponseBody
    public String uploadPhoto(MultipartFile file){
        return UploadUtil.upload(file,uploadpath);
    }
}
