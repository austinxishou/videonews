package com.fuicuiedu.xc.videonew_20170309.ui.likes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.NewsEntity;
import com.fuicuiedu.xc.videonew_20170309.commons.CommonUtils;
import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;
import com.fuicuiedu.xc.videonew_20170309.ui.base.BaseItemView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**新闻的实体类*/

public class LikesItemView extends BaseItemView<NewsEntity>{

    public LikesItemView(Context context) {
        super(context);
    }


    /**单行布局的控件*/
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt;

    /**单行布局使用的实体类数据*/
    private NewsEntity newsEntity;

    /**———————————————————————————————————————抽象方法—————————————————————————————————————————————
     * 实现单行布局抽象类实现的抽象方法————————（initview初始化填充布局）和（bindmodel将数据绑定到控件上）*/
    @Override
    protected void initView() {
        LayoutInflater.from(getContext()).inflate(
                R.layout.item_likes,this,true);
        ButterKnife.bind(this);
    }

    /**拿到实体类——将数据绑定到控件上*/
    @Override
    protected void bindModel(NewsEntity newsEntity) {
        this.newsEntity = newsEntity;

        tvNewsTitle.setText(newsEntity.getNewsTitle());
        /**将日期转换为固定格式的日期*/
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));
        /**使用Picasso————————加载预览图片*/
        String url = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        Picasso.with(getContext()).load(url).into(ivPreview);
    }
/**————————————————————————————————————————————————————————————————————————————————————*/


    /**因为是单行布局，所以可以直接实现单行布局的监听——实现点击某一行出发事件*/
    @OnClick
    public void onClick(){
        //// TODO: 2017/3/22 0022 跳转到评论页面
        ToastUtils.showShort("跳转到评论页面");
    }


}
