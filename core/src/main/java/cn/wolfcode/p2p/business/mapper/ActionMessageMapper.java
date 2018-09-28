package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.ActionMessage;
import cn.wolfcode.p2p.business.query.ActionMessageQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActionMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActionMessage entity);

    List<ActionMessage> selectById(Long id);

    ActionMessage selectBySms(Long id);

    void updateState(Long id);

    int getBySmsId(@Param("b") boolean b, @Param("id") Long id);


    /*int queryForCount(ActionMessageQueryObject qo);

    List<ActionMessage> queryForList(@Param("qo") ActionMessageQueryObject qo,@Param("userId") Long userId);*/
}