package com.fuicuiedu.xc.videonew_20170309.ui.local;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  继承游标的cursor的适配器
 * 看类的继承关系，，，ctrl + h
 */

public class LocalVideoAdapter extends CursorAdapter{

    /** 用来加载视频预览图的线程池*/
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**用来缓存已经加载过的预览图像*/
    private LruCache<String,Bitmap> lruCache = new LruCache<String, Bitmap>(5 * 1024 * 1024){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return super.sizeOf(key, value);
        }
    };

    /**构造方法（上下文，游标，是否要自动获取数据）*/
    public LocalVideoAdapter(Context context) {
        super(context, null, true);
    }

    /**创建子视图——返回已创好的单行布局*/
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new LocalVideoItem(context);
    }


    /**绑定数据*/
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /**将view强转成想要的单行布局LocalVideoItem，并用LocalVideoItem对象来接收————并用LocalVideoItem对象绑定参数中的游标*/
        final LocalVideoItem item = (LocalVideoItem) view;
        item.bind(cursor);

        /**从item中获取视频的路径*/
        final String filePath = item.getFilePath();

        /**去缓存中获取预览图——第一次缓存区中肯定为空，所以bitmap也为空*/
        Bitmap bitmap = lruCache.get(filePath);

        /**有视频路径的时候（即后台线程获取到了预览图），视频显示的图片设置成预览图*/
        if (bitmap != null){
            //设置预览图
            item.setIvPreView(bitmap);
            return;
        }

//        /**后台线程获取视频预览图*/
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
//                //缓存当期的预览图,文件路径作为key
//                lruCache.put(filePath,bitmap);
//                //将预览图设置到控件上
//                //注意，当前是在后台线程
//                item.setIvPreView(filePath,bitmap);
//            }
//        });
    }

    /**关闭线程池——shutdown*/
    public void release(){
        executorService.shutdown();
    }
}
