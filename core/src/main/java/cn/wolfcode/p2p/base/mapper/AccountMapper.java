package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountMapper {

    int insert(Account entity);

    Account selectByPrimaryKey(Long id);


    int updateByPrimaryKey(Account entity);

    BigDecimal totalUnReceiveInterest();
}