package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import com.sun.tools.jdi.LongTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 视频认证审核
 */
@Controller
public class VideoAuthController {

    @Autowired
    private IVideoAuthService videoAuthService;

    /**
     * 审核列表
     * @param model
     * @param qo
     * @return
     */
    @RequestMapping("vedioAuth")
    public String vedioAuth(Model model, @ModelAttribute("qo")VideoAuthQueryObject qo){
        qo.setAuditorId(UserContext.getLoginInfo().getId());
        PageResult pageResult =  videoAuthService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "vedioAuth/list";
    }

    /**
     * 提交审核
     */
    @RequestMapping("vedioAuth_audit")
    @ResponseBody
    public JSONResult vedioAuthAudit(Long id,String remark,int state){
        JSONResult jsonResult = new JSONResult();
        try {
            videoAuthService.audit(id,remark,state);
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
