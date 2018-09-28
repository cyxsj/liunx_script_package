package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.mapper.IpLogMapper;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IpLogServiceImpl implements IIpLogService {

    @Autowired
    private IpLogMapper ipLogMapper;

    public void save(int userType,String username,String ip, int state) {
        IpLog ipLog = new IpLog();
        ipLog.setState(state);
        ipLog.setLoginTime(new Date());
        ipLog.setUsername(username);
        ipLog.setUserType(userType);
        ipLog.setIp(ip);
        ipLogMapper.insert(ipLog);
    }

    @Override
    public PageResult query(IpLogQueryObject qo) {
        int count = ipLogMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }
        List<IpLog> list = ipLogMapper.queryForList(qo);
        return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
    }
}
