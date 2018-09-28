package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.LoginInfo;

import java.util.List;

public interface ILoginInfoService {
    /**
     * 根据id获取登录对象
     * @param id
     * @return
     */
    LoginInfo selectById(Long id);

    /**
     * 前台用户注册
     * @param username
     * @param verifycode 验证密码
     * @param password 密码
     * @param confirmPwd 确认密码
     */
    void regidter(String username, String verifycode, String password, String confirmPwd,Long recommend);

    boolean existUsername(String username);

    /**
     * 用户登录
     * @param username
     * @param password
     * @param isMgrsite
     */
    void login(String username, String password,boolean isMgrsite);

    /**
     * 根据用户类型查询列表
     * @param userType
     * @return
     */
    List<LoginInfo> listByUserType(int userType);


}
