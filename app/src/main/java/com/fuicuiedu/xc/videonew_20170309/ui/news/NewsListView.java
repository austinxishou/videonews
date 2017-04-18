package com.fuicuiedu.xc.videonew_20170309.ui.news;

import android.content.Context;
import android.util.AttributeSet;

import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.NewsEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.QueryResult;
import com.fuicuiedu.xc.videonew_20170309.ui.base.BaseResourceView;

import retrofit2.Call;

/**
 * 自定义的listview————传入实体类和单行布局
 */

public class NewsListView extends BaseResourceView<NewsEntity,NewsItemView>{

    public NewsListView(Context context) {
        super(context);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**查询的数据类型——newsentity实体类*/
    @Override
    protected Call<QueryResult<NewsEntity>> queryData(int limit, int skip) {
        return newsApi.getVideoNewsList(limit, skip);
    }

    /**每次请求的数据量*/
    @Override
    protected int getLimit() {
        return 5;
    }

    /**创建不同的单行布局*/
    @Override
    protected NewsItemView createItemView() {
        return new NewsItemView(getContext());
    }
}
