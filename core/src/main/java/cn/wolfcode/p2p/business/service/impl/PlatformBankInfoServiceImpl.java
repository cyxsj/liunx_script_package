package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.mapper.PlatformBankInfoMapper;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService {

    @Autowired
    private PlatformBankInfoMapper platformBankInfoMapper;

    @Override
    public PageResult query(QueryObject qo) {
        int count = platformBankInfoMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }
        List<PlatformBankInfo> list = platformBankInfoMapper.queryForList(qo);
        return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void saveOrUpdate(PlatformBankInfo info) {
        if (info.getId() == null){
            platformBankInfoMapper.insert(info);
        }else {
            platformBankInfoMapper.updateByPrimaryKey(info);
        }
    }

    @Override
    public List<PlatformBankInfo> selectAll() {
        return platformBankInfoMapper.selectAll();
    }
}
