package com.fuicuiedu.xc.videonew_20170309.ui.news.comments;

import android.content.Context;
import android.util.AttributeSet;

import com.fuicuiedu.xc.videonew_20170309.bombapi.BombConst;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.CommentsEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.other.InQuery;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.QueryResult;
import com.fuicuiedu.xc.videonew_20170309.ui.base.BaseResourceView;

import retrofit2.Call;

public class CommentsListView extends BaseResourceView<CommentsEntity,CommentsItemView>{
    public CommentsListView(Context context) {
        super(context);
    }

    public CommentsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**新闻的id*/
    public String newsId;

    public void setNewsId(String newsId){
        this.newsId = newsId;
    }

    /**获取所有新闻评论的方法*/
    @Override
    protected Call<QueryResult<CommentsEntity>> queryData(int limit, int skip) {
        InQuery where = new InQuery(BombConst.FIELD_NEWS,BombConst.TABLE_NEWS,newsId);
        return newsApi.getComments(limit,skip,where);
    }

    @Override
    protected int getLimit() {
        return 20;
    }

    @Override
    protected CommentsItemView createItemView() {
        return new CommentsItemView(getContext());
    }
}
