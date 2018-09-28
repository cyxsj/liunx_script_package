package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.List;

public interface ISystemDictionaryService {

    /**
     * 根据分类的编号sn,查询下面的明细
     * @param sn
     * @return
     */
    List<SystemDictionaryItem> listItemBySn(String sn);

    /**
     * 数据字典目录分类的分页查询
     * @param qo
     * @return
     */
    PageResult queryDir(SystemDictionaryQueryObject qo);

    /**
     * 保存/修改数据
     * @param entity
     */
    void saveOrUpdate(SystemDictionary entity);

    /**
     *
     * @return
     */
    List<SystemDictionary> queryAllDir();

}
