package cn.wolfcode.controller;

import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IBidService;
import cn.wolfcode.p2p.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
/**
 * 首页相关
 */
public class IndexController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountService accountService;


    /**
     * 首页
     */

    @RequestMapping("index")
    public String index(Model model){
        //发标公告
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setBidRequestState(Constants.BIDREQUEST_STATE_PUBLISH_PENDING);
        //限定显示5条数据
        qo.setPageSize(5);
        List<BidRequest> publishPendngBidRequests = bidRequestService.queryForLisr(qo);
        model.addAttribute("publishPendngBidRequests",publishPendngBidRequests);

        //进行中的借款
        BidRequestQueryObject tempQo = new BidRequestQueryObject();
        tempQo.setBidRequestStates(new int[]{Constants.BIDREQUEST_STATE_BIDDING
                ,Constants.BIDREQUEST_STATE_COMPLETE_PAY_BACK
                ,Constants.BIDREQUEST_STATE_PAYING_BACK});
        //排序
        tempQo.setOrderBy("br.bidRequestState");
        tempQo.setPageSize(5);


        /**
         * 总投资人数:
         * 总借款人数:
         * 总借款金额:
         * 已收到的总收益:
         * 总待收本息:
         */

        int totalUser = bidService.selectAll();
        //总投资人数
        model.addAttribute("totalUser",totalUser);
        //总借款人数
        model.addAttribute("totalCreateUser",bidRequestService.totalCreateUser());
        //总借款金额
        model.addAttribute("totalBidRequestAmount",bidRequestService.totalBidRequestAmount());
        //已收到的总收益
        model.addAttribute("totalRewardAmount",bidRequestService.totalRewardAmount());
        //总待收本息
        model.addAttribute("totalUnReceiveInterest",accountService.totalUnReceiveInterest());

        List<BidRequest> bidRequests = bidRequestService.queryForLisr(tempQo);
        model.addAttribute("bidRequests",bidRequests);
        return "main";
    }

}
