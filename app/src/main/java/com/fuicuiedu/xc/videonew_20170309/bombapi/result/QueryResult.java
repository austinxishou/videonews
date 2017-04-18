package com.fuicuiedu.xc.videonew_20170309.bombapi.result;

import java.util.List;

/**
 *  查询结果
 *  实体可以是新闻数据，可以是评论数据，所以做一个泛型————传入实体类
 */

public class QueryResult<Model> {

    private List<Model> results;

    public List<Model> getResults() {
        return results;
    }
}
