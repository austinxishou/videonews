package com.fuicuiedu.xc.videoplayer.part;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fuicuiedu.xc.videoplayer.R;
import com.fuicuiedu.xc.videoplayer.full.VideoViewActivity;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * s使用的是vitamio的视频播放videoplayer，并且自定义的VideoPlayer，使用Mediaplayer + SurfaceView来实现视频的播放
 * Mediaplayer来做视频播放的控制，SurfaceView用来显示视频
 * 视图方面，一个播放、暂停按钮，一个进度条，一个全屏按钮，一个SurfaceView
 *
 * 结构：
 * 提供setVideoPath方法：设置数据源
 * 提供OnResume方法（在Activity的onResume调用）：初始化Mediaplayer，准备Mediaplayer
 * 提供OnPause方法（在Activity的OnPause调用）:释放Mediaplayer，停止播放
 */

public class SimpleVideoPlayer extends FrameLayout{    //继承的是framelayout

    //进度条控制（长度）
    private static final int PROGRESS_MAX = 1000;

    private String videoPath;//视频路径
    private MediaPlayer mediaPlayer;
    private boolean isPrepared;//是否准备好
    private boolean isPlaying;//是否正在播放

    //视图相关——surfaceView和surfaceHolder用来播放视频
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView ivPreView;//预览图
    private ImageButton btnToggle;//播放，暂停
    private ProgressBar progressBar;//进度条
    
    public SimpleVideoPlayer(Context context) {
        this(context,null);
    }

    public SimpleVideoPlayer(Context context, AttributeSet attrs) {   //如果是自定义的，就不需要继承super，用this，记得第三个参数是0
        this(context, attrs,0);
    }

    public SimpleVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();//构造方法中视图初始化相关
    }

    /**
     *
     *   第一步：构造方法中——视图初始化相关
     *   初始化vitamio，将布局填充进去
     */
    private void init() {
        Vitamio.isInitialized(getContext());
        LayoutInflater.from(getContext()).
                inflate(R.layout.view_simple_video_player,this,true);
        //初始化SurfaceView
        initSurfaceView();
        //初始化视频播放控制视图
        initControllerViews();
    }

    /**
     * 准备的东西
     * 初始化SurfaceView————注意，使用Vitamio要设置像素格式，否则会花屏
     */
    private void initSurfaceView(){
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }
    /**
     *     设置数据源
     */
    public void setVideoPath(String videoPath){
        this.videoPath = videoPath;
    }



    /**
     *  初始化视频播放控制视图
     *  初始化预览图，初始化进度条
     *  暂停的按钮，点击的时候暂停播放视频
     *  初始化全屏播放按钮，点击全屏播放视频
     */
    private void initControllerViews(){
        //预览图
        ivPreView = (ImageView) findViewById(R.id.ivPreview);
        //播放，暂停
        btnToggle = (ImageButton) findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否正在播放
                if (mediaPlayer.isPlaying()){
                    /**
                     *    暂停播放
                     */
                    pauseMediaplayer();
                }else if (isPrepared){
                    /**
                     *    开始播放
                     */
                    startMediaplayer();
                }else{
                    Toast.makeText(getContext(), "Can't play now！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         *   设置进度条,设置进度条的最大值PROGRESS_MAX
         */
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(PROGRESS_MAX);
        /**
         * 全屏播放按钮——跳转到新的activity
         */
        findViewById(R.id.btnFullScreen).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 跳转到全屏播放
                 */
                VideoViewActivity.open(getContext(),videoPath);
            }
        });
    }

    /**activity中调用的方法
     * ——————————————————————————————————————————————————————————————————————————————————————————————————————————
     *  初始化状态(在Activity的onResume调用)
     *  初始化mediaplayer和mediaplayer的准备3步走
     */
    public void onResume(){
        //初始化MediaPlayer
        initMediaPlayer();
        //准备MediaPlayer
        prepareMediaPlayer();
    }

    /**
     * 释放状态(在Activity的onPause调用)
     */
    public void onPause(){
        //暂停MediaPlayer
        pauseMediaplayer();
        //释放MediaPlayer
        releaseMediaPlayer();
    }


    /** 逻辑代码
     *  初始化MediaPlayer
     * ——————————————————————————————————————————————————————————————————————————————————————————————————————————
     */
    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer(getContext());
        mediaPlayer.setDisplay(surfaceHolder);
        //准备监听——准备好的时候就播放视频
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                startMediaplayer();
            }
        });
        //audio处理
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_FILE_OPEN_OK){
                    mediaPlayer.audioInitedOk(mediaPlayer.audioTrackInit());
                    return true;
                }
                return false;
            }
        });
        /**
         *   视频大小改变监听————此处设置surfaceview的大小
         */
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                //参数的宽和高，是指视频的宽和高，我们可以通过参数去设置SurfaceView宽高
                int layoutWidth = surfaceView.getWidth(); //先得到surfaceview的宽，然后通过视频的宽高比去得到surfaceview的高
                int layoutHeight = layoutWidth * height / width;  //layoutHeight/layoutWidth  = height / width;
                //更新SurfaceView的宽高
                ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
                params.width = layoutWidth;
                params.height = layoutHeight;
                surfaceView.setLayoutParams(params);
            }
        });
    }

    //准备MediaPlayer——异步准备
    private void prepareMediaPlayer(){
        try {
            //重置Mediaplayer
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoPath);
            //设置循环播放
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            ivPreView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            Log.e("SimpleVideoPlayer","prepare Mediaplayer" + e.getMessage());
        }
    }

    /**
     *  开始播放——通过handler来更新进度条
     */
    private void startMediaplayer(){
        ivPreView.setVisibility(View.INVISIBLE);
        btnToggle.setImageResource(R.drawable.ic_pause);
        mediaPlayer.start();
        isPlaying = true;
        //进度条操作
        handler.sendEmptyMessage(0);
    }

    /**
     *   暂停播放——remove。handler，不更新进度条
     */

    private void pauseMediaplayer(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        isPlaying = false;
        btnToggle.setImageResource(R.drawable.ic_play_arrow);
        //进度条操作
        handler.removeMessages(0);
    }

    //使用handler更新进度条
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //每0.2秒更新一下进度条
            if (isPlaying){
                /**
                 *  如果在播放视频，实时获取进度条的进度
                 *  当前的位置除以视频的总长度（duration），在诚意最大值，就是当前的进度条的进度
                 */
                int progress = (int)(mediaPlayer.getCurrentPosition() *
                        PROGRESS_MAX / mediaPlayer.getDuration());
                progressBar.setProgress(progress);
                //发送一个空的延迟消息，不停的调用本身，实现自动更新进度条
                handler.sendEmptyMessageDelayed(0,200);
            }
        }
    };

    //释放MediaPlayer
    private void releaseMediaPlayer(){
        mediaPlayer.release();
        mediaPlayer = null;
        isPrepared = false;
        progressBar.setProgress(0);
    }


}
