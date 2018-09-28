package cn.wolfcode.web.controller;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.util.JSONResult;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据字典相关
 */
@Controller
public class SystemDictionaryController {

    /**
     * 数据字典目录
     */
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @RequestMapping("systemDictionary_list")
    public String systemDictionaryList(Model model, @ModelAttribute("qo") SystemDictionaryQueryObject qo) {
        PageResult pageResult = systemDictionaryService.queryDir(qo);
        model.addAttribute("pageResult", pageResult);
        return "systemdic/systemDictionary_list";
    }

    @RequestMapping("systemDictionary_update")
    @ResponseBody
    public JSONResult systemDictionaryUpdate(SystemDictionary entity) {
        JSONResult jsonResult = new JSONResult();
        try {
            systemDictionaryService.saveOrUpdate(entity);
        } catch (DisplayableException e) {
            e.printStackTrace();
            jsonResult.mark(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }


    /**
     * 数据字典明细相关
     */

    @RequestMapping("systemDictionaryItem_list")
    public String systemDictionaryItemList(Model model, @ModelAttribute("qo") SystemDictionaryQueryObject qo){
        PageResult pageResult = systemDictionaryItemService.queryItem(qo);
        model.addAttribute("pageResult",pageResult);
        //数据字典分类
        List<SystemDictionary> systemDictionaryGroups = systemDictionaryService.queryAllDir();
        model.addAttribute("systemDictionaryGroups",systemDictionaryGroups);
        return "systemdic/systemDictionaryItem_list";
    }


    @RequestMapping("systemDictionaryItem_update")
    @ResponseBody
    public JSONResult systemDictionaryItemUpdate(SystemDictionaryItem entity) {
        JSONResult jsonResult = new JSONResult();
        try {
            systemDictionaryItemService.saveOrUpdate(entity);
        } catch (DisplayableException e) {
            e.printStackTrace();
            jsonResult.mark(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.mark("系统异常了,我们正在处理老杨");
        }
        return jsonResult;
    }
}
