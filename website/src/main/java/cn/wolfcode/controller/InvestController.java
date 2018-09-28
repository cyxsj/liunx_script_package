package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 投资页面
 */
@Controller
public class InvestController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IRealAuthService realAuthService;



    /**
     * 我要投资页面
     * @return
     */
    @RequestMapping("invest")
    public String invest(){
        return "invest";
    }

    /**
     * 我要投资列表数据
     */
    @RequestMapping("invest_list")
    public String investList(Model model, @ModelAttribute("qo") BidRequestQueryObject qo){

        if (qo.getBidRequestState() == -1){
            qo.setBidRequestStates(new int[]{Constants.BIDREQUEST_STATE_BIDDING
                    ,Constants.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        }
        PageResult pageResult = bidRequestService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "invest_list";
    }


    /**
     * 投标详情页面
     */
    @RequestMapping("borrow_info")
    public String borrowInfo(Long id,Model model){

        BidRequest bidRequest = bidRequestService.get(id);

        UserInfo userInfo = userInfoService.get(bidRequest.getCreateUser().getId());

        RealAuth realAuth = realAuthService.get(userInfo.getRealAuthId());


        LoginInfo currentLoginInfo = UserContext.getLoginInfo();
        //如果有登录
        if (currentLoginInfo != null){
            Account account = accountService.get(currentLoginInfo.getId());
            model.addAttribute("account",account);

            //如果是借款人登录
            if(currentLoginInfo.getId().equals(bidRequest.getCreateUser())){
                model.addAttribute("self",true);
            }
        }

        model.addAttribute("bidRequest",bidRequest);
        model.addAttribute("userInfo",userInfo);
        model.addAttribute("realAuth",realAuth);

        return "borrow_info";
    }

    /**
     * 投标
     */
    @RequestMapping("borrow_bid")
    @ResponseBody
    public JSONResult borrowBid(Long bidRequestId, BigDecimal amount){
        JSONResult jsonResult = new JSONResult();
        try {
            bidRequestService.bid(bidRequestId,amount);
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
