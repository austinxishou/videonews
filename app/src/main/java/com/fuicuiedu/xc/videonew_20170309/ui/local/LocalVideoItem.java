package com.fuicuiedu.xc.videonew_20170309.ui.local;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videoplayer.full.VideoViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  自定义布局
 */

public class LocalVideoItem extends FrameLayout{


    public LocalVideoItem(Context context) {
        this(context,null);
    }

    /**this 第三个参数改为0，层层调用*/
    public LocalVideoItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LocalVideoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**构造方法中填充布局——填充布局之后buttrerknife绑定*/
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_local_video,this,true);  //改为true
        ButterKnife.bind(this);
    }

    /**————————————————————————————————————————————————————————————————————————————
     * 预览图——textview——文件的路径*/
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvVideoName)
    TextView tvVideoName;
    private String filePath;

    /**获取视频的路径*/
    public String getFilePath(){
        return filePath;
    }

    /**设置预览图——bitmap*/
    public void setIvPreView(Bitmap bitmap){
        ivPreview.setImageBitmap(bitmap);
    }

    /**设置预览图的方法，可以在后台线程执行*/
    public void setIvPreView(String filePath,final Bitmap bitmap){
        if (!filePath.equals(this.filePath)) return;
        /**直接post通知更新ui*/
        post(new Runnable() {
            @Override
            public void run() {
                ivPreview.setImageBitmap(bitmap);
            }
        });
    }

     /**传入游标,游标寻找视频，并拿到视频的路径和视频的名字*/
    public void bind(Cursor cursor){
        //取出视频名称
        int index = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
        String videoName = cursor.getString(index);
        tvVideoName.setText(videoName);
        //取出视频路径
        filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

        //获取视频预览图，是一个很费事的操作
        //-------放到后台去执行

        //同时获取多张图片，可能会同时又多个线程执行
        //-------线程池来控制

        //获取过的图片，做缓存处理
        //-------LruCache(最近最少使用原则)
    }

    /**点击item，全屏播放*/
    /**点击item，获取视频的路径*/
    @OnClick
    public void click(){
        Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();
//        VideoViewActivity.open(getContext(),filePath);
    }
}
