package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginInfoMapper {

    LoginInfo selectById(Long id);

    void inset(LoginInfo loginInfo);

    /**
     * 根据用户名查询条数
     * @param username
     * @return
     */
    int countByUsername(String username);

    LoginInfo login(@Param("username") String username, @Param("password") String password);

    List<LoginInfo> listByUserType(int userType);

}
