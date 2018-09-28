package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.BaseAuditDomain;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.business.query.RechargeQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {

    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

    public void apply(RechargeOffline recharge) {
        //参数判断
        //保存充值申请
        RechargeOffline newRecharge = new RechargeOffline();

        newRecharge.setAmount(recharge.getAmount());
        newRecharge.setBankInfo(recharge.getBankInfo());
        newRecharge.setTradeCode(recharge.getTradeCode());
        newRecharge.setTradeTime(recharge.getTradeTime());
        newRecharge.setNote(recharge.getNote());
        newRecharge.setApplier(UserContext.getLoginInfo());
        newRecharge.setApplyTime(new Date());

        rechargeOfflineMapper.insert(newRecharge);
    }

    @Override
    public PageResult query(RechargeQueryObject qo) {
        int count = rechargeOfflineMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }
        List<RechargeOffline> list = rechargeOfflineMapper.queryForList(qo);
        return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void audit(Long id, int state, String remark) {
        //判断充值对象状态
        RechargeOffline recharge = rechargeOfflineMapper.selectByPrimaryKey(id);
        if (recharge.getState() != BaseAuditDomain.STATE_NORMAL) {
            throw new DisplayableException("充值状态不处于待审核");
        }

        //设置审核相关信息
        recharge.setAuditor(UserContext.getLoginInfo());
        recharge.setAuditTime(new Date());
        recharge.setRemark(remark);
        recharge.setState(state);
        update(recharge);

        if (state == BaseAuditDomain.STATE_SUCCESS){
            //审核成功
            //修改充值用户的可用余额增加
            Account account = accountService.get(recharge.getApplier().getId());
            account.setUsableAmount(account.getUsableAmount().add(recharge.getAmount()));
            accountService.update(account);
            //添加充值成功流水
            accountFlowService.createRechargeOfflineFlow(account,recharge.getAmount());
        }
    }

    private void update(RechargeOffline recharge) {
        int count = rechargeOfflineMapper.updateByPrimaryKey(recharge);
        if (count == 0) {
            throw new DisplayableException("充值信息修改失败");
        }
    }
}
