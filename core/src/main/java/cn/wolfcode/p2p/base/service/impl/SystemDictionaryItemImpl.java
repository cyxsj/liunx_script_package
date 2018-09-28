package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemDictionaryItemImpl implements ISystemDictionaryItemService{

    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    /**
     * 数据字典明细分页查询
     * @param qo
     * @return
     */
    public PageResult queryItem(SystemDictionaryQueryObject qo) {
        int count = systemDictionaryItemMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }

        return new PageResult(systemDictionaryItemMapper.queryForList(qo),
                count,qo.getCurrentPage(),qo.getPageSize());
    }

    public void saveOrUpdate(SystemDictionaryItem entity) {
        if (entity.getId() != null){
            systemDictionaryItemMapper.updateByPrimaryKey(entity);
        }else {
            systemDictionaryItemMapper.insert(entity);
        }
    }
}
