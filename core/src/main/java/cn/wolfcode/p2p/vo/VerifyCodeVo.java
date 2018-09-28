package cn.wolfcode.p2p.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用户保存发送验证码记录
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeVo {
    //验证码
    private String code;
    //手机号码
    private String phoneNumber;
    //发送时间
    private Date sendTime;
}
