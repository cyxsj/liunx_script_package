package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.mapper.LoginInfoMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.*;
import cn.wolfcode.p2p.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
/**
 * ApplicationListener<ContextRefreshedEvent>
 *     实现后台方法
 */
public class LoginInfoServiceImpl implements ILoginInfoService,ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;



    public LoginInfo selectById(Long id) {
        return loginInfoMapper.selectById(id);
    }

    /**
     * 注册验证
     * @param username 用户名是否已注册
     * @param verifycode 验证密码
     * @param password 密码
     * @param confirmPwd 确认密码
     */
    public void regidter(String username, String verifycode, String password, String confirmPwd,Long recommend) {
        //参数判断
        AsserUtil.isNull(username,"用户名不能为空");
        AsserUtil.isNull(verifycode,"验证码不能为空");
        AsserUtil.isNull(password,"密码不能为空");
        AsserUtil.isEquals(password,confirmPwd,"两次密码输入不一致");

        //验证码校验
        VerifyCodeVo vo = UserContext.getVerifyCode();
        if (vo == null){
            throw new DisplayableException("验证码已失效,请重新发送");
        }
        if (!vo.getCode().equals(verifycode)) {
            throw new RuntimeException("验证码已失效,请重新发送");
        }
        //接收验证的手机号和注册时的手机号要一致
        if (!vo.getPhoneNumber().equals(username)) {
            throw new DisplayableException("接收验证码的手机号和当前注册ed手机号不一致,请重新输入");
        }
        //验证码失效时间校验
        boolean canRegister = DateUtil.getSecondsBetween(vo.getSendTime(), new Date())>Constants.VERIFYCODE_SEND_TIME_VALID;
        if (canRegister) {
            throw new DisplayableException("验证码失效,请重新发送");
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        //给密码加密
        loginInfo.setPassword(MD5.encode(password+username));


        //查询推荐码是否存在数据库
        LoginInfo user = loginInfoMapper.selectById(recommend);

        if (user.getId() != null){

            //保存推荐人id
            loginInfo.setRecommend(recommend);
        }

        loginInfoMapper.inset(loginInfo);


        //初始化account,因为id是传入来的,所以要做初始化
        accountService.init(loginInfo.getId());

        //初始化userInfo,因为id是传入来的,所以要做初始化
        userInfoService.init(loginInfo.getId(),username);
    }

    public boolean existUsername(String username) {
        int count = loginInfoMapper.countByUsername(username);
        return count > 0;
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    public void login(String username, String password,boolean isMgrsite) {
        //判断基本参数
        AsserUtil.isNull(username,"用户名不能为空");
        AsserUtil.isNull(password,"密码不能为空");
        //执行登录
        LoginInfo loginInfo = loginInfoMapper.login(username, MD5.encode(password+username));
        AsserUtil.isObjNull(loginInfo,"用户名或密码错误");

        //userType用来标记是前台用户/后台用户
        if (isMgrsite && !loginInfo.isMgrSiteUser()){
            throw new DisplayableException("用户类型错误");
        }
        if (!isMgrsite && loginInfo.isMgrSiteUser()){
            throw new DisplayableException("用户类型错误");
        }

        //登录对象放入session
        UserContext.setLoginInfo(loginInfo);
    }




    public static void main(String[] args) {
        System.out.println(MD5.encode("111111老杨"));
    }

    //初始化第一个管理员
    private void initAdmin() {

        //判断是否已经存在
        if(existUsername("admin"))return;

        LoginInfo info = new LoginInfo();
        info.setUsername("admin");
        info.setPassword(MD5.encode("111111admin"));
        info.setUserType(LoginInfo.USERTYPE_MGRSITE);
        loginInfoMapper.inset(info);
    }

    /**
     * 后台用户登录
     * @param event
     */
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initAdmin();
    }


    public List<LoginInfo> listByUserType(int userType) {
        return loginInfoMapper.listByUserType(userType);
    }

}
