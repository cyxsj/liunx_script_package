package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemDictionaryQueryObject extends QueryObject{
    private String keyword;

    //分类id
    private Long parentId = -1L;
}
