package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleDetailMapper;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {

    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

    public void update(PaymentScheduleDetail detail) {
        int count = paymentScheduleDetailMapper.updateByPrimaryKey(detail);
        if (count == 0) {
            throw new DisplayableException("收款计划修改失败");
        }
    }
}
