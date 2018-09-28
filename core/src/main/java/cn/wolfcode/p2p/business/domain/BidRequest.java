package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.util.Constants;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 借款相关后台处理
 */
@Getter
@Setter
public class BidRequest extends BaseAuditDomain{
    //乐观锁
    private int version;
    //还款方式
    private int returnType;
    //借款类型
    private int bidRequestType;
    //借款状态
    private int bidRequestState;
    //借款金额
    private BigDecimal bidRequestAmount;
    //借款利率
    private BigDecimal currentRate;
    //最小投标金额
    private BigDecimal minBidAmount;
    //借款期限
    private int monthes2Return;
    //投标进度
    private int bidCount;
    //投标总回报金额  总利息
    private BigDecimal totalRewardAmount;
    //投多少钱
    private BigDecimal currentSum = Constants.ZERO;
    //借款标题
    private String title;
    //借款描述
    private String description;
    //风控审核
    private String note;
    //招标期限
    private Date disableDate;
    //招标天数
    private int disableDays;
    //借款人
    private LoginInfo createUser;
    //借款的投标记录
    private List<Bid> bids = new ArrayList<Bid>();
    //申请时间
    private Date applyTime;
    //定时发标
    private Date publishTime;


    //还需金额
    public BigDecimal getRemainAmount(){
        return bidRequestAmount.subtract(this.getCurrentSum());
    }


    //投标进度
    public int getPersent(){
        return getCurrentSum().divide(getBidRequestAmount(),Constants.SCALE_CAL,
                RoundingMode.HALF_UP).multiply(new BigDecimal("100.00")).intValue();
    }


    public String getJsonString(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        map.put("username",createUser.getUsername());
        map.put("title",title);
        map.put("bidRequestAmount",bidRequestAmount);
        map.put("currentRate",currentRate);
        map.put("monthes2Return",monthes2Return);
        map.put("returnType",returnType);
        map.put("totalRewardAmount",totalRewardAmount);

        return JSON.toJSONString(map);
    }

    public String getReturnTypeDisplay(){
        return returnType == Constants.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL?"按月分期":"按月到期";
    }


    public String getBidRequestStateDisplay(){
        switch (bidRequestState){
            case Constants.BIDREQUEST_STATE_APPLY :return "申请中";
            case Constants.BIDREQUEST_STATE_PUBLISH_PENDING :return "待发布";
            case Constants.BIDREQUEST_STATE_BIDDING :return "招标中";
            case Constants.BIDREQUEST_STATE_UNDO :return "已撤销";
            case Constants.BIDREQUEST_STATE_BIDDING_OVERDUE : return "流标";
            case Constants.BIDREQUEST_STATE_APPROVE_PENDING_1 :return "满标1审";
            case Constants.BIDREQUEST_STATE_APPROVE_PENDING_2 :return "满标2审";
            case Constants.BIDREQUEST_STATE_REJECTED  :return "满标审核被拒绝";
            case Constants.BIDREQUEST_STATE_PAYING_BACK :return "还款中";
            case Constants.BIDREQUEST_STATE_COMPLETE_PAY_BACK :return "已还清";
            case Constants.BIDREQUEST_STATE_PAY_BACK_OVERDUE :return "逾期";
            case Constants.BIDREQUEST_STATE_PUBLISH_REFUSE :return "发标审核拒绝状态";
        }
        return "未知";
    }



}
