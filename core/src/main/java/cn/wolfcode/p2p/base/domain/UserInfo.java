package cn.wolfcode.p2p.base.domain;

import cn.wolfcode.p2p.util.BitStatesUtils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfo extends BaseDomain{

    private int version = 0;
    //位状态
    private Long bitState = 0L;
    //姓名
    private String realName;
    //省份证
    private String idNumber;
    //电话
    private String phoneNumber;
    //邮箱
    private String email;

    //保存实名认证id,在做申请的时候设置值,实名审核失败的时候设置为null
    private Long realAuthId;

    //以下字段查询数据字典
    private SystemDictionaryItem incomeGrade;

    private SystemDictionaryItem marriage;

    private SystemDictionaryItem kidCount;

    private SystemDictionaryItem educationBackground;

    private SystemDictionaryItem houseCondition;

    //判断是否有借款在申请流程中
    public boolean isBidRequestProcess(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
    }

    //是否已经完成了基本资料填写
    public boolean isBasicInfo(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.OP_BASIC_INFO);
    }

    //是否已经完成了实名认证
    public boolean isRealAuth(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.OP_REAL_AUTH);
    }

    //是否已经完成了视频认证
    public boolean isVedioAuth(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.OP_VEDIO_AUTH);
    }

    //是否满足借款条件
    public boolean canBorrow(){
        return isBasicInfo() && isRealAuth() && isVedioAuth();
    }
}