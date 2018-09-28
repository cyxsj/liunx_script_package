package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.ui.Model;

public interface IIpLogService {

    /**
     * 保存登录日志
     */
    void save(int userType,String username,String ip, int state);

    /**
     * 日志分页
     * @param qo
     * @return
     */
    PageResult query(IpLogQueryObject qo);

}
