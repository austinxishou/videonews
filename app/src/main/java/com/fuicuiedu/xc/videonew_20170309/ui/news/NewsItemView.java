package com.fuicuiedu.xc.videonew_20170309.ui.news;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.NewsEntity;
import com.fuicuiedu.xc.videonew_20170309.commons.CommonUtils;
import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;
import com.fuicuiedu.xc.videonew_20170309.ui.base.BaseItemView;
import com.fuicuiedu.xc.videonew_20170309.ui.news.comments.CommentActivity;
import com.fuicuiedu.xc.videoplayer.list.MediaPlayerManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻列表的单项视图，将使用Mediaplayer控制视频，Textureview来显示视频
 */

public class NewsItemView extends BaseItemView<NewsEntity> implements
        MediaPlayerManager.OnPlaybackListener,
        TextureView.SurfaceTextureListener{

    /**展示视频的textview*/
    @BindView(R.id.textureView)
    TextureView textureView;
    /**黑的预览图*/
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    /**显示的视频标题*/
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    /**显示视频上传的时间*/
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt;
    /**视频加载时候显示的进度条*/
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**一开始时候显示的播放的图片*/
    @BindView(R.id.ivPlay)
    ImageView ivPlay;

    /**解析出来的新闻实体类数据*/
    private NewsEntity newsEntity;
    /**视频播放用到的vitamio中的mediaplayermanager和surface*/
    private MediaPlayerManager mediaPlayerManager;
    private Surface surface;


    /**构造方法*/
    public NewsItemView(Context context) {
        super(context);
    }


    /**__________________________________________________start__________________________________________________________________
     * 继承baseitemview实现的2抽象方法
     * */
    @Override
    protected void initView() {
        /**填充布局+butterknife*/
        LayoutInflater.from(getContext()).inflate(R.layout.item_news,this,true);
        ButterKnife.bind(this);

        /**初始化列表播放的管理类——————整个recyclerview中只只用一个mediaplayer对象来播放视频*/
        mediaPlayerManager = MediaPlayerManager.getsInstance(getContext());
        /**添加列表视频播放控制相关监听*/
        mediaPlayerManager.addPlayerBackListener(this);
        /**surface相关监听*/
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void bindModel(NewsEntity newsEntity) {
        this.newsEntity = newsEntity;
        /**初始化视图状态——标题，预览图，播放的图片*/
        tvNewsTitle.setVisibility(View.VISIBLE);
        ivPreview.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        /**设置标题，创建时间，预览图*/
        tvNewsTitle.setText(newsEntity.getNewsTitle());
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));
        /**设置预览图像（Picasso）,服务器返回带中文的图片地址需要转换*/
        String url = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        Picasso.with(getContext()).load(url).into(ivPreview);
    }
    /**_______________________________________________________end____________________________________________________________*/




    /**点击按钮，跳转到评论页面————跳转到评论页面(把新闻的实体类传过去)*/
    @OnClick(R.id.tvCreatedAt)
    public void onClick(){
        CommentActivity.open(getContext(),newsEntity);
    }

    /**点击预览图，开始播放*/
    @OnClick(R.id.ivPreview)
    public void startPlayer(){
        /**如果surface为空，什么都不做*/
        if (surface == null) return;
        /**拿到视频的路径和视屏的id*/
        String path = newsEntity.getVideoUrl();
        String videoId = newsEntity.getObjectId();

        /**调用开始播放视频的方法——————传入相对应的surface对象，视频的路径，视频的id*/
        mediaPlayerManager.startPlayer(surface,path,videoId);
    }

    /**点击播放视频的texttrurview，停止播放*/
        @OnClick(R.id.textureView)
        public void stopPlayer(){
        mediaPlayerManager.stopPlayer();
    }

    /**判断是否操作当前的视频————防止视频播放错乱的问题*/
    private boolean isCurrentVideo(String videoId){
        /**添加特殊情况的判断——如果点击实体类中的视频id和路径为空的话————返回false（掉膘）*/
        if (videoId == null || newsEntity == null) return false;
        return videoId.equals(newsEntity.getObjectId());
    }

    /**######################    添加列表视频播放控制相关监听start   ####################*/
    /**如果确实是播放当前的视频——将当前视频的prb显示出来*/
    @Override
    public void onStartBuffering(String videoId) {
        if (isCurrentVideo(videoId)) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    /**如果确实是播放当前的视频——隐藏进度条*/
    @Override
    public void onStopBuffering(String videoId) {
        if (isCurrentVideo(videoId)) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    /**开始播放当前的视频*/
    @Override
    public void onStartPlay(String videoId) {
        if (isCurrentVideo(videoId)) {
            /**播放视频的时候——隐藏标题，预览图，开始播放的按钮，进度条*/
            tvNewsTitle.setVisibility(View.INVISIBLE);
            ivPreview.setVisibility(View.INVISIBLE);
            ivPlay.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    /**停止播放*/
    @Override
    public void onStopPlay(String videoId) {
        if (isCurrentVideo(videoId)) {
            /**显示标题，预览图，播放的按钮，隐藏进度条*/
            tvNewsTitle.setVisibility(View.VISIBLE);
            ivPreview.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    /**无需求，不做处理*/
    @Override
    public void onSizeMeasured(String videoId, int width, int height) {
    }
    /**######################    添加列表视频播放控制相关监听over   ####################*/




    /**#####################      surface相关监听start  #######################*/
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = new Surface(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.surface.release();
        this.surface = null;
        /**停止自己*/
        if (newsEntity.getObjectId().equals(mediaPlayerManager.getVideoId())) {
            mediaPlayerManager.stopPlayer();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
    /**#####################      surface相关监听over  #######################*/
}
