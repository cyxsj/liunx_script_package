package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
/**
 * 借款相关
 */
public class BidRequestController {

    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 发标审核页面
     * @return
     */
    @RequestMapping("bidrequest_publishaudit_list")
    public String bidrequestPublishauditList(Model model, @ModelAttribute("qo") BidRequestQueryObject qo){

        qo.setBidRequestState(Constants.BIDREQUEST_STATE_APPLY);
        PageResult pageResult = bidRequestService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "bidrequest/publish_audit";
    }

    /**
     * 发标审核
     */
    @RequestMapping("bidrequest_publishaudit")
    @ResponseBody
    public JSONResult bidrequestPublishaudit(Long id, String remark, int state,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date publishTime){

        JSONResult jsonResult = new JSONResult();
        try {
            bidRequestService.publishAudit(id,remark,state,publishTime);
        }catch (DisplayableException e){
             e.printStackTrace();
             jsonResult.mark(e.getMessage());
        }catch (Exception e){
             e.printStackTrace();
             jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }

    /**
     * 满标一审页面
     */
    @RequestMapping("bidrequest_audit1_list")
    public String bidrequestAudit1List(Model model,@ModelAttribute("qo") BidRequestQueryObject qo){
        qo.setBidRequestState(Constants.BIDREQUEST_STATE_APPROVE_PENDING_1);
        PageResult pageResult = bidRequestService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "bidrequest/audit1";
    }

    /**
     * 满标一审
     */
    @RequestMapping("bidrequest_audit1")
    @ResponseBody
    public JSONResult bidrequestAudit1(Long id,int state,String remark){
        JSONResult jsonResult = new JSONResult();
        try {
            bidRequestService.audit1(id,state,remark);
        }catch (DisplayableException e){
             e.printStackTrace();
             jsonResult.mark(e.getMessage());
        }catch (Exception e){
             e.printStackTrace();
             jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }


    /**
     * 满标二审页面
     */
    @RequestMapping("bidrequest_audit2_list")
    public String bidrequestAudit2List(Model model,@ModelAttribute("qo") BidRequestQueryObject qo){
        qo.setBidRequestState(Constants.BIDREQUEST_STATE_APPROVE_PENDING_2);
        PageResult pageResult = bidRequestService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "bidrequest/audit2";
    }


    /**
     * 满标一审
     */
    @RequestMapping("bidrequest_audit2")
    @ResponseBody
    public JSONResult bidrequestAudit2(Long id,int state,String remark){
        JSONResult jsonResult = new JSONResult();
        try {
            bidRequestService.audit2(id,state,remark);
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
