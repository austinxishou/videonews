package com.fuicuiedu.xc.videonew_20170309.ui.likes;

import android.content.Context;
import android.util.AttributeSet;

import com.fuicuiedu.xc.videonew_20170309.UserManager;
import com.fuicuiedu.xc.videonew_20170309.bombapi.BombConst;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.NewsEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.other.InQuery;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.QueryResult;
import com.fuicuiedu.xc.videonew_20170309.ui.base.BaseResourceView;

import retrofit2.Call;

/**
 * 我的收藏类表视图
 */

public class LikesListView extends BaseResourceView<NewsEntity,LikesItemView>{
    public LikesListView(Context context) {
        super(context);
    }

    public LikesListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikesListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**—————————————————————————————继承共有的类——实现3个接口————————————————————————————————————
     * 请求数据（创建一个请求）*/
    @Override
    protected Call<QueryResult<NewsEntity>> queryData(int limit, int skip) {
        /**由于服务器原因造成的参数（可以直接使用）————————————需要用到where参数*/
        String userId = UserManager.getInstance().getObjectId();
        InQuery where = new InQuery(BombConst.FIELD_LIKES,BombConst.TABLE_USER,userId);
        /**返回的是api中返回的call类型*/
        return newsApi.getLikedList(limit,skip,where);
    }

    /** 每次请求多少条数据*/
    @Override
    protected int getLimit() {
        return 15;
    }

    /**实例化ItemView*/
    @Override
    protected LikesItemView createItemView() {
        LikesItemView likesItemView = new LikesItemView(getContext());
        return likesItemView;
    }
    /**——————————————————————————————————————————————————————————————————————————*/

    /**退出登录时，清空收藏列表*/
    public void clear(){
        adapter.clear();
    }
}
