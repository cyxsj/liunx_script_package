package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.mapper.AccountMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;


    public void init(Long id) {
        Account account = new Account();
        account.setId(id);
        accountMapper.insert(account);
    }

    public Account get(Long userId) {
        Account account = accountMapper.selectByPrimaryKey(userId);
        return account;
    }


    public void update(Account account) {
        int count = accountMapper.updateByPrimaryKey(account);
        if (count == 0){
            throw new DisplayableException("账户信息修改失败[乐观锁异常id:"+account.getId()+"]");
        }
    }

    public BigDecimal totalUnReceiveInterest() {
        return accountMapper.totalUnReceiveInterest();
    }

}
