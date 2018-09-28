package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.business.domain.ActionMessage;
import cn.wolfcode.p2p.business.mapper.ActionMessageMapper;
import cn.wolfcode.p2p.business.query.ActionMessageQueryObject;
import cn.wolfcode.p2p.business.service.IActionMessageService;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ActionMessageServiceImpl implements IActionMessageService {
    @Autowired
    private ActionMessageMapper actionMessageMapper;

    public void actionMessage(LoginInfo receiver, String title, String context, Date noteTime) {
        ActionMessage message = new ActionMessage();
        message.setReceiver(receiver);
        message.setTitle(title);
        message.setContext(context);
        message.setNoteTime(new Date());
        actionMessageMapper.insert(message);
    }

    public List<ActionMessage> get(Long id) {
        return actionMessageMapper.selectById(id);
    }

    public ActionMessage getEmail(Long id) {
       return actionMessageMapper.selectBySms(id);
    }

    public void updateState(Long id) {
        actionMessageMapper.updateState(id);
    }

    public int getBySmsId(boolean b, Long id) {
        return actionMessageMapper.getBySmsId(b,id);
    }

    /*public PageResult query(ActionMessageQueryObject qo) {
        Long userId = UserContext.getLoginInfo().getId();
        int count = actionMessageMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }
        List<ActionMessage> list = actionMessageMapper.queryForList(qo,userId);
        return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
    }*/
}
