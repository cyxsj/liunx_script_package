package cn.wolfcode.p2p.base.service;

public interface ISendVerifyCodeService {
    /**
     * 发送短信验证码
     * @param phoneNumber
     */
    void send(String phoneNumber);
}
