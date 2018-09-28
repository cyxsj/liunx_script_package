package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
/**
 * 借款相关
 */
public class BorrowController {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService ;


    @RequestMapping("borrow")
    public String borrow(Model model){
        //如果没有登录,跳转到借款首页静态页面
        LoginInfo loginInfo = UserContext.getLoginInfo();
        if (loginInfo == null) {
            return "redirect:/borrowpage.html";
        }
        UserInfo userInfo = userInfoService.get(loginInfo.getId());
        model.addAttribute("account",accountService.get(loginInfo.getId()));
        model.addAttribute("userinfo",userInfo);

        return "borrow";
    }

    /**
     * 借款申请页面
     * @param model
     * @return
     */
    @RequestMapping("borrowInfo")
    public String borrowInfo(Model model){

        //用户账户
        Long userId = UserContext.getLoginInfo().getId();

        //如果已经有借款申请在流程中
        UserInfo userInfo = userInfoService.get(userId);
        if (userInfo.isBidRequestProcess()) {
            return "borrow_apply_result";
        }

        //最小借款金额
        model.addAttribute("minBidRequestAmount", Constants.BORROW_MIN_AMOUNT);
        Account account = accountService.get(userId);
        model.addAttribute("account",account);

        //最大/最小利率
        model.addAttribute("minRate",Constants.BORROW_MIN_RATE);
        model.addAttribute("maxRate",Constants.BORROW_MAX_RATE);

        //最小投标金额
        model.addAttribute("minBidAmount",Constants.BID_MIN_AMOUNT);

        return "borrow_apply";
    }

    /**
     * 借款申请提交
     */
    @RequestMapping("borrow_apply")
    @ResponseBody
    public JSONResult borrowApply(BidRequest br){
        JSONResult jsonResult = new JSONResult();
        try {
            bidRequestService.apply(br);
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
     * 还款列表
     */
    @RequestMapping("borrowBidReturn_list")
    public String borrowBidReturnList(Model model, @ModelAttribute("qo") PaymentScheduleQueryObject qo){
        PageResult pageResult = paymentScheduleService.query(qo);
        model.addAttribute("pageResult",pageResult);

        //用户账户
        Account account = accountService.get(UserContext.getLoginInfo().getId());
        model.addAttribute("account",account);

        return "returnmoney_list";
    }

    /**
     * 执行还款
     */
    @RequestMapping("returnMoney")
    @ResponseBody
    public JSONResult returnMoney(Long id){
        JSONResult jsonResult = new JSONResult();
        try {
            bidRequestService.returnMoney(id);
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
