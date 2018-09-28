package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.UserInfo;
import java.util.List;

public interface UserInfoMapper {

    int insert(UserInfo entity);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(UserInfo entity);

    /**
     * 修改用户的基本信息
     * @param userInfo
     * @return
     */
    int updateBadicInfo(UserInfo userInfo);
}