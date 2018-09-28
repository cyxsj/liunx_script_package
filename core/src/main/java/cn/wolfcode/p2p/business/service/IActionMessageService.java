package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.business.domain.ActionMessage;
import cn.wolfcode.p2p.business.query.ActionMessageQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.Date;
import java.util.List;

public interface IActionMessageService {
    /**
     * 发送站内消息
     */
    void actionMessage(LoginInfo receiver, String title, String context, Date noteTime);

    /**
     * 站内消息列表
     * @param id
     * @return
     */
    List<ActionMessage> get(Long id);

    /**
     * 查看信息
     * @param id
     */
    ActionMessage getEmail(Long id);

    /**
     * 查看后改为已读状态
     * @param id
     */
    void updateState(Long id);

    /**
     * 显示未读信息
     * @param b
     * @param id
     * @return
     */
    int getBySmsId(boolean b, Long id);

//    PageResult query(ActionMessageQueryObject qo);
}
