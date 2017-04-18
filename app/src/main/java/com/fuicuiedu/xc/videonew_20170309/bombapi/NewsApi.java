package com.fuicuiedu.xc.videonew_20170309.bombapi;

import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.CommentsEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.NewsEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.PublishEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.other.InQuery;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.CollectResult;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.CommentsResult;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.QueryResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *  新闻相关网络接口
 */

public interface NewsApi {

    /**获取新闻列表,排序方式，接时间新到旧排序*/
    @GET("1/classes/News?order=-createdAt")
    Call<QueryResult<NewsEntity>> getVideoNewsList(
            @Query("limit") int limit,
            @Query("skip") int skip);

    /**获取新闻的所有评论，按时间从新到旧来排序*/
    /**注意，我们希望评论作者不仅返回objectId，还返回username，可以使用URL编码参数include=author*/
    @GET("1/classes/Comments?include=author&order=-createdAt")
    Call<QueryResult<CommentsEntity>> getComments(
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("where") InQuery where);

    /**发表评论——传入评论的实体类（commentsresult）*/
    @POST("1/classes/Comments")
    Call<CommentsResult> postComments(@Body PublishEntity publishEntity);

    /**获取收藏列表——————————————————传入限定值（每次获取多少数据）——*/
    @GET("1/classes/News?order=-createdAt")
    Call<QueryResult<NewsEntity>> getLikedList(
            @Query("limit") int limit, //限定值，限制每次获取网络数据的个数
            @Query("skip") int skip,   //刚开始开始的数据位数
            @Query("where") InQuery where
    );

    /**收藏新闻用到的call*/
    @GET("bef74a37a08d3205/changeLike?action=like")
    Call<CollectResult> collectNews(
            @Query("newsId") String newsId,
            @Query("userId") String userId
    );
}
