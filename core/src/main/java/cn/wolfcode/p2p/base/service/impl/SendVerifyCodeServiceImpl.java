package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.service.ISendVerifyCodeService;
import cn.wolfcode.p2p.util.*;
import cn.wolfcode.p2p.vo.VerifyCodeVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class SendVerifyCodeServiceImpl implements ISendVerifyCodeService {
    public void send(String phoneNumber) {
        //手机号码判断
        AsserUtil.isNull(phoneNumber,"无效的手机号码,请重新输入!");
        //发送频繁判断,上一次发送时间(session)-当前时间=时间间隔
        VerifyCodeVo verifyCode = UserContext.getVerifyCode();
        Date now = new Date();
        if (verifyCode != null) {
            //稍后可以执行发送
            boolean canSend = DateUtil.getSecondsBetween(verifyCode.getSendTime(),
                    now)> Constants.VERIFYCODE_SEND_TIME;
                AsserUtil.isTrue(!canSend,"发送频繁,请稍后重试");
        }
        //生成验证码
        String code = UUID.randomUUID().toString().substring(0,4);

        //执行发送
        System.out.println("您的手机验证码是:"+code);
        //调用下面的方法执行发送短信验证码
        //sendSms(phoneNumber, code);

        //保存发送记录到session中
        VerifyCodeVo vo = new VerifyCodeVo(code,phoneNumber,new Date());
        UserContext.setVerifyCode(vo);
    }
    /*public void sendSms(String phoneNumber, String code){
        Map<String,String> map = new HashMap<String, String>();
        //https://way.jd.com/Decent/smsSend?mobile=18912345678¶m=code:1234&tpl_id=TP1711162&appkey=您申请的APPKEY
//        map.put("msg","【成都创信】验证码为："+code+",欢迎注册平台！");
        map.put("msg","【中正云通信】您的JD验证码为："+code+"，欢迎注册使用。");

        map.put("tos",phoneNumber);
        map.put("appkey","d651eeb45b2882988e06cd99120accb0");
        try {
            String s = HttpUtil.sendHttpRequest("https://way.jd.com/jixintong/hyapi", map);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
