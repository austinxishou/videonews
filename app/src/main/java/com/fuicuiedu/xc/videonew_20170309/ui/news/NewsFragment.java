package com.fuicuiedu.xc.videonew_20170309.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videoplayer.list.MediaPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment{


    /**使用的是自定义的listview*/
    @BindView(R.id.newsListView)
    NewsListView newsListView;

    /**全局变量的view*/
    private View view;


    /**oncreatview创建view方法中，填充布局*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_news,container,false);
        }
        return view;
    }

    /**onviewcreat方法中，绑定控件*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        /**首次进来，自动刷新————————控件直接调用post方法，更新ui*/
        newsListView.post(new Runnable() {
            @Override
            public void run() {
                /**newsListView继承了baseresouseview，所以有自动刷新的方法*/
                newsListView.autoRefresh();
            }
        });
    }


    /**onresume方法中————初始化Mediaplayer*/
    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.getsInstance(getContext()).onResume();
    }

    /**在onpause方法中，Mediaplayer释放*/
    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.getsInstance(getContext()).onPause();
    }

    /**ondestoryview中————移除View（填充的布局）*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)view.getParent()).removeView(view);
    }

    /**清除所有监听（不再需要Ui交互）*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getsInstance(getContext()).removeAllListeners();
    }
}
