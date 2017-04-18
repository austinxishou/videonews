package com.fuicuiedu.xc.videonew_20170309.ui.news.comments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.UserManager;
import com.fuicuiedu.xc.videonew_20170309.bombapi.BombClient;
import com.fuicuiedu.xc.videonew_20170309.bombapi.NewsApi;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.NewsEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.CollectResult;
import com.fuicuiedu.xc.videonew_20170309.commons.CommonUtils;
import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;
import com.fuicuiedu.xc.videoplayer.part.SimpleVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private static final String KEY_NEWS = "KEY_NEWS";

    /**对外公开一个跳转进来的方法*/
    public static void open(Context context, NewsEntity newsEntity){
        Intent intent = new Intent(context,CommentActivity.class);
        intent.putExtra(KEY_NEWS,newsEntity);
        /**通过context对象，调用startactivity方法*/
        context.startActivity(intent);
    }

    /**拿到toolbar，toolbar中textview对象*/
    @BindView(R.id.tvTitle)TextView tvTitle;
    @BindView(R.id.toolbar)Toolbar toolbar;
    /**部分播放视频的videoplayer和评论的listview*/
    @BindView(R.id.simpleVideoPlayer)SimpleVideoPlayer simpleVideoPlayer;
    @BindView(R.id.commentsListView)CommentsListView commentsListView;

    /**新闻实体类的对象*/
    private NewsEntity newsEntity;
    private EditCommentFragment editCommentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        /**拿到新闻数据——————————————————intent传递对象，通过getSerializableExtra获得*/
        newsEntity = (NewsEntity) getIntent().getSerializableExtra(KEY_NEWS);
        /**设置toolbar*/
        setSupportActionBar(toolbar);
        /**给左上角加返回图标(返回按钮)*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**设置标题*/
        tvTitle.setText(newsEntity.getNewsTitle());
        /**初始化simpleVideoPlayer，设置数据源*/
        String videoPath = CommonUtils.encodeUrl(newsEntity.getVideoUrl());
        simpleVideoPlayer.setVideoPath(videoPath);



        /**初始化commentsListView，设置newsid————并且自动刷新新闻下方的评论列表*/
        commentsListView.setNewsId(newsEntity.getObjectId());
        commentsListView.autoRefresh();
    }

    /**#######################   视频相关  #######################*/
    @Override
    protected void onResume() {
        super.onResume();
        simpleVideoPlayer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleVideoPlayer.onPause();
    }
    /**#######################   视频相关  #######################*/



    /**##########################  toolbar相关  ############################*/
    /**创建一个菜单栏————-菜单栏需要提前在res下的menu包中创建好*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.activity_comments,menu);
        return true;
    }

    /**实现toolbar中不同位置的点击效果*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**左上角返回按钮*/
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        /**判断用户是否登录*/
        if(UserManager.getInstance().isOffline()){
            ToastUtils.showShort(R.string.please_login_first);
            return true;
        }
        /**收藏*/
        if (item.getItemId() == R.id.menu_item_like){
            /**拿到新闻的id和用户的id*/
            String newsId = newsEntity.getObjectId();
            String userId = UserManager.getInstance().getObjectId();
            /**拿到收藏新闻用到的api——用call接收（call里面传入得到）*/
            NewsApi newsApi_cloud = BombClient.getInstance().getNewsApi_cloud();
            Call<CollectResult> call = newsApi_cloud.collectNews(newsId,userId);
            call.enqueue(callback);
        }
        /**评论*/
        if (item.getItemId() == R.id.menu_item_comment){
            /**创建dialogfragment*/
            if (editCommentFragment == null){
                /**拿到新闻的id*/
                String newsid = newsEntity.getObjectId();
                /**实例化评论的dialoggragment（单例）*/
                editCommentFragment = EditCommentFragment.getInstance(newsid);
                /**添加dialogfragment的监听*/
                editCommentFragment.setListener(listener);
            }
            /**显示评论界面的dialogfragment*/
            editCommentFragment.show(getSupportFragmentManager(),"Edit Comment");
        }
        return super.onOptionsItemSelected(item);
    }
    /**#########################  toolbar相关  ############################*/



    /**收藏用到的callback——内部类的形式*/
    private Callback<CollectResult> callback = new Callback<CollectResult>() {
        @Override
        public void onResponse(Call<CollectResult> call, Response<CollectResult> response) {
            CollectResult result = response.body();
            if (result.isSuccess()){
                ToastUtils.showShort(R.string.like_success);
            }else{
                ToastUtils.showShort(R.string.like_failure + result.getError());
            }
        }

        @Override
        public void onFailure(Call<CollectResult> call, Throwable t) {
            ToastUtils.showShort(t.getMessage());
        }
    };

    /**dialogfragment界面的监听方法*/
    EditCommentFragment.OnCommentSuccessListener listener =new EditCommentFragment.OnCommentSuccessListener() {
        @Override
        /**评论成功后——隐藏评论的界面，然后更新评论列表界面*/
        public void onCommentSuccess() {
            editCommentFragment.dismiss();
            commentsListView.autoRefresh();
        }
    };
}
