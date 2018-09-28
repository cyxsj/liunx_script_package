package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class BaseAuditDomain extends BaseDomain {
    //审核状态:待审核
    public static final int STATE_NORMAL=0;
    //审核成功
    public static final int STATE_SUCCESS=1;
    //审核失败
    public static final int STATE_REJECT=2;

    public String getStateDisplay(){
        switch (state){
            case STATE_NORMAL:return "待审核";
            case STATE_SUCCESS:return "审核成功";
            case STATE_REJECT:return "审核失败";
        }
        return "未知";
    }



    //审核状态
    protected int state = STATE_NORMAL;

    //申请人
    protected LoginInfo applier;

    //审核备注
    protected String remark;

    //审核人
    protected LoginInfo auditor;

    //申请时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date applyTime;

    //审核时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date auditTime;

}
