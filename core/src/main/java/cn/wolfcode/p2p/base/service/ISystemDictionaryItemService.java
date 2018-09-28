package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface ISystemDictionaryItemService {
    /**
     * 数据字典明细分类的分页查询
     * @param qo
     * @return
     */
    PageResult queryItem(SystemDictionaryQueryObject qo);

    /**
     * 保存/修改数据
     * @param entity
     */
    void saveOrUpdate(SystemDictionaryItem entity);
}
