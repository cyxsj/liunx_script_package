package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VideoAuth entity);

    VideoAuth selectByPrimaryKey(Long id);

    List<VideoAuth> selectAll();

    int updateByPrimaryKey(VideoAuth entity);

    VideoAuth getByStateAudUserId(@Param("state") int state,@Param("userId") Long userId);

    int queryForCount(VideoAuthQueryObject qo);

    List queryForList(VideoAuthQueryObject qo);
}