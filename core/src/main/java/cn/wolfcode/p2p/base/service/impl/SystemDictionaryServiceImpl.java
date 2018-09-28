package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class SystemDictionaryServiceImpl implements ISystemDictionaryService{

    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    public List<SystemDictionaryItem> listItemBySn(String sn) {

        return systemDictionaryItemMapper.listItemBySn(sn);
    }


    public PageResult queryDir(SystemDictionaryQueryObject qo) {
        int count = systemDictionaryMapper.queryForCount(qo);
        if (count == 0){
            return PageResult.empty(qo.getPageSize());
        }

        return new PageResult(systemDictionaryMapper.queryForList(qo),count,qo.getCurrentPage(),qo.getPageSize());
    }

    public void saveOrUpdate(SystemDictionary entity) {
        if (entity.getId() != null){
            systemDictionaryMapper.updateByPrimaryKey(entity);
        }else {
            systemDictionaryMapper.insert(entity);
        }
    }

    @Override
    public List<SystemDictionary> queryAllDir() {
        return systemDictionaryMapper.selectAll();
    }
}
