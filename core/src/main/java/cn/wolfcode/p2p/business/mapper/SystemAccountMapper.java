package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.SystemAccount;
import java.util.List;

public interface SystemAccountMapper {

    int insert(SystemAccount entity);

    SystemAccount selectByPrimaryKey(Long id);


    int updateByPrimaryKey(SystemAccount entity);

    SystemAccount getCurrent();

}