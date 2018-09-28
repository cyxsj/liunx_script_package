package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryObject {
    private int currentPage = 1;
    private int pageSize = 5;

    //排序字段
    private String orderBy;
    private String orderType;

    public int getStart(){
        return pageSize * (this.currentPage -1);
    }

}
