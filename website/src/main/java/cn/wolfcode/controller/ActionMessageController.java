package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.ActionMessage;
import cn.wolfcode.p2p.business.query.ActionMessageQueryObject;
import cn.wolfcode.p2p.business.service.IActionMessageService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 站内系统消息
 */
@Controller
public class ActionMessageController {

    @Autowired
    private IActionMessageService actionMessageService;

    /**
     * 站内消息页面
     * @return
     */
    @RequestMapping("actionMessage")
    @NeedLogin
    public String actionMessage(Model model){

        List<ActionMessage> actionMessages = actionMessageService.get(UserContext.getLoginInfo().getId());

        model.addAttribute("actionMessage",actionMessages);

        return "email";
    }

    /**
     * 未读信息
     */
    @RequestMapping("getState")
    @ResponseBody
    public int getState(){
        int totalState = actionMessageService.getBySmsId(false,UserContext.getLoginInfo().getId());
        return totalState;
    }


    /**
     * 查看信息
     */
    @RequestMapping("email_info")
    @NeedLogin
    public String emailInfo(Model model,Long id){

        actionMessageService.updateState(id);
        model.addAttribute("email",actionMessageService.getEmail(id));
        return "email_info";
    }



    /**
     * 消息列表
     */

   /* @RequestMapping("actionMessageList")
    @ResponseBody
    public JSONResult actionMessageList(Model model){
        JSONResult jsonResult = new JSONResult();
        try {
        }catch (DisplayableException e){
             e.printStackTrace();
             jsonResult.mark(e.getMessage());
        }catch (Exception e){
             e.printStackTrace();
             jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }
*/
}
