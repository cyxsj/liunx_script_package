package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.OrderTime;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.IOrderTimeService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.util.DateUtil;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.UserContext;
import com.alibaba.druid.filter.AutoLoad;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 视频认证相关
 */
@Controller
public class VideoAuthController {

    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private IOrderTimeService orderTimeService;
    @Autowired
    private IVideoAuthService videoAuthService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 视频预约页面
     * @return
     */
    @RequestMapping("videoAuditOrder")
    public String videoAuditOrder(Model model){

        //如果已经有语句,查询预约对象
        LoginInfo loginInfo = UserContext.getLoginInfo();

        UserInfo userInfo = userInfoService.get(loginInfo.getId());
        if (userInfo.isVedioAuth()) {
            //用户已经完成视频认证
            model.addAttribute("success",true);
            return "videoOrder";
        }

        VideoAuth tempVideoAuth = videoAuthService.getByStateAudUserId(VideoAuth.STATE_NORMAL, loginInfo.getId());
        if (tempVideoAuth != null) {
            //用户有视频申请
            model.addAttribute("videoAuth",tempVideoAuth);
            return "videoOrder";
        }

        //查询审核客服
        List<LoginInfo> auditors = loginInfoService.listByUserType(LoginInfo.USERTYPE_AUDITOR);
        model.addAttribute("auditors",auditors);
        //可预约的日期
        List<Date> orderDates = new ArrayList<Date>();
        Date now = new Date();
        orderDates.add(now);
        orderDates.add(DateUtils.addDays(now,1));
        orderDates.add(DateUtils.addDays(now,2));
        orderDates.add(DateUtils.addDays(now,3));
        //可以预约日期
        model.addAttribute("orderDates",orderDates);

        //可以预约时间段
        List<OrderTime> orderTimes = orderTimeService.selectAll();
        model.addAttribute("orderTimes",orderTimes);
        return "videoOrder";
    }


    /**
     * 视频认证预约申请
     */
    @RequestMapping("apply")
    @ResponseBody
    public JSONResult Apply(Long auditorId,String orderDate,Long timeId){
        JSONResult jsonResult = new JSONResult();
        try {
            videoAuthService.apply(auditorId,orderDate,timeId);
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
