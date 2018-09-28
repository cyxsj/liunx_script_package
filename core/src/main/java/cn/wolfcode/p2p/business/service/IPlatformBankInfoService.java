package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.util.PageResult;

import java.util.List;

public interface IPlatformBankInfoService {
    /**
     * 分页查询
     * @param qo
     * @return
     */
    PageResult query(QueryObject qo);

    /**
     * 保存/修改银行卡
     * @param info
     */
    void saveOrUpdate(PlatformBankInfo info);

    /**
     * 查询平台所有的银行卡账号
     * @return
     */
    List<PlatformBankInfo> selectAll();
}
