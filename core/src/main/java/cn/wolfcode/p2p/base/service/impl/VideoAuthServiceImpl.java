package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.OrderTime;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.mapper.VideoAuthMapper;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.base.service.IOrderTimeService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.base.service.IVideoAuthService;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.DateUtil;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;

@Service
@Transactional
public class VideoAuthServiceImpl implements IVideoAuthService{

    @Autowired
    private VideoAuthMapper videoAuthMapper;
    @Autowired
    private IOrderTimeService orderTimeService;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void apply(Long auditorId, String orderDate, Long timeId) {
        //参数基本判断

        //判断用户是否已经有预约
        LoginInfo loginInfo = UserContext.getLoginInfo();
        VideoAuth tempVideoAuth = videoAuthMapper.getByStateAudUserId(VideoAuth.STATE_NORMAL,loginInfo.getId());
        if (tempVideoAuth != null){
            throw new DisplayableException("您已经有预约中的视频认证,请稍等!");
        }

        //判断用户是否已经完成视频认证
        UserInfo userInfo = userInfoService.get(loginInfo.getId());
        if (userInfo.isVedioAuth()) {
            throw new DisplayableException("您已经完成了视频审核");
        }

        OrderTime orderTime = orderTimeService.get(timeId);
        //保存视频认证对象
        VideoAuth videoAuth = new VideoAuth();
        videoAuth.setApplier(UserContext.getLoginInfo());
        videoAuth.setApplyTime(new Date());
        videoAuth.setState(VideoAuth.STATE_NORMAL);

        //09:   09:30
        //2018-04-13 09:00:00   2018-04-13 09:30:00
        String beginDateStr = orderDate+" "+ orderTime.getBegin()+":00";
        String endDateStr = orderDate+" "+ orderTime.getEnd()+":00";
        videoAuth.setOrderBeginDate(DateUtil.parseDate(beginDateStr));
        videoAuth.setOrderEndDate(DateUtil.parseDate(endDateStr));

        LoginInfo tempLoginInfo = new LoginInfo();
        tempLoginInfo.setId(auditorId);
        videoAuth.setAuditor(tempLoginInfo);

        videoAuthMapper.insert(videoAuth);
    }

    @Override
    public VideoAuth getByStateAudUserId(int state, Long id) {
        return videoAuthMapper.getByStateAudUserId(state,id);
    }

    @Override
    public PageResult query(VideoAuthQueryObject qo) {
        int count = videoAuthMapper.queryForCount(qo);
        if (count==0) {
            return PageResult.empty(qo.getPageSize());
        }
        return new PageResult(videoAuthMapper.queryForList(qo),count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //视频认证状态是否处于待审核
        VideoAuth videoAuth = videoAuthMapper.selectByPrimaryKey(id);
        if (videoAuth.getState() != VideoAuth.STATE_NORMAL) {
            throw new DisplayableException("审核对象不处于待审核状态");
        }

        //设置审核相关信息
        videoAuth.setAuditTime(new Date());
        videoAuth.setState(state);
        videoAuth.setRemark(remark);
        update(videoAuth);
        //如果审核通过
        //修改userInfo.birstate,添加视频审核通过状态
        if (state == VideoAuth.STATE_SUCCESS) {
            UserInfo userInfo = userInfoService.get(videoAuth.getApplier().getId());
            if (userInfo.isVedioAuth()) {
                throw new DisplayableException("用户已经完成了视频认证");
            }
            long tempState = BitStatesUtils.addState(userInfo.getBitState(), BitStatesUtils.OP_VEDIO_AUTH);
            userInfo.setBitState(tempState);

            userInfoService.update(userInfo);
        }
        //如果审核失败

    }

    private void update(VideoAuth videoAuth) {
        int count = videoAuthMapper.updateByPrimaryKey(videoAuth);
        if (count == 0){
            throw new DisplayableException("视频审核信息修改失败");
        }
    }
}
