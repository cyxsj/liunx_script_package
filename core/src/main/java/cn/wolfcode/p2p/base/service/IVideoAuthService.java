package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface IVideoAuthService {
    /**
     * 视频认证预约
     * @param auditorId : 预约的客服
     * @param orderDate : 预约的日期
     * @param timeId : 预约的时间段
     */
    void apply(Long auditorId, String orderDate, Long timeId);

    /**
     * 根据类型查询用户的视频预约对象
     * @param state
     * @param id
     * @return
     */
    VideoAuth getByStateAudUserId(int state,Long id);


    /**
     * 后台视频认证审核列表分页查询
     * @param qo
     * @return
     */
    PageResult query(VideoAuthQueryObject qo);

    /**
     * 视频认证审核提交
     * @param id
     * @param remark
     * @param state
     */
    void audit(Long id, String remark, int state);
}
