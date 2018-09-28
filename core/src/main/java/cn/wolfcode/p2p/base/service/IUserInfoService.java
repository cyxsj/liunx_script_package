package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.UserInfo;

public interface IUserInfoService {
    /**
     * 初始化userInfo
     * @param id
     * @param phoneNumber 电话号码
     */
    void init(Long id,String phoneNumber);

    /**
     * 根据id获取userinfo对象
     * @param id
     * @return
     */
    UserInfo get(Long id);

    void basicInfoSave(UserInfo userInfo);

    /**
     * 修改userInfo
     * @param userInfo
     */
    void update(UserInfo userInfo);
}
