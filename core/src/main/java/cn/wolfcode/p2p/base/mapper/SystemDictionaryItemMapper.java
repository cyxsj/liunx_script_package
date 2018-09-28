package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface SystemDictionaryItemMapper {

    int insert(SystemDictionaryItem entity);

    SystemDictionaryItem selectByPrimaryKey(Long id);


    int updateByPrimaryKey(SystemDictionaryItem entity);

    List<SystemDictionaryItem> listItemBySn(String sn);

    int queryForCount(SystemDictionaryQueryObject qo);

    List queryForList(SystemDictionaryQueryObject qo);
}