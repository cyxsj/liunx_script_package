package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.mapper.AccountMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.mapper.BidMapper;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BidServiceImpl implements IBidService {

    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private AccountMapper accountMapper;

    public BigDecimal sumUserBidAmountByBidRequestId(Long bidRequestId, Long userId) {
        return bidMapper.sumUserBidAmountByBidRequestId(bidRequestId,userId);
    }

    public void save(BidRequest br, BigDecimal amount, LoginInfo currentUser) {
        Bid bid = new Bid();
        bid.setActualRate(br.getCurrentRate());
        bid.setAvailableAmount(amount);
        bid.setBidRequestId(br.getId());
        bid.setBidRequestState(br.getBidRequestState());
        bid.setBidRequestTitle(br.getTitle());
        bid.setBidTime(new Date());
        bid.setBidUser(currentUser);
        bidMapper.insert(bid);
    }


    public void batchUpdateState(Long bidRequestId, int state) {
        bidMapper.batchUpdateState(bidRequestId,state);
    }


    public void update(Account account) {
        accountMapper.updateByPrimaryKey(account);
    }

    public int selectAll() {
        return bidMapper.selectAll();
    }

    public Bid get(Long recommend) {
        return bidMapper.selectByPrimaryKey(recommend);
    }
}
