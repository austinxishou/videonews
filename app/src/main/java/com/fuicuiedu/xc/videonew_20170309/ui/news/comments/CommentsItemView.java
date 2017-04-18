package com.fuicuiedu.xc.videonew_20170309.ui.news.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.AuthorEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.CommentsEntity;
import com.fuicuiedu.xc.videonew_20170309.commons.CommonUtils;
import com.fuicuiedu.xc.videonew_20170309.ui.base.BaseItemView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class CommentsItemView extends BaseItemView<CommentsEntity>{

    @BindView(R.id.tvContent)
    TextView tvContent; // 评论内容
    @BindView(R.id.tvAuthor)
    TextView tvAuthor; // 评论作者
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt; // 评论时间

    public CommentsItemView(Context context) {
        super(context);
    }


    /**填充布局*/
    @Override
    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_comments,this,true);
        ButterKnife.bind(this);
    }

    /**绑定数据*/
    @Override
    protected void bindModel(CommentsEntity commentsEntity) {
        String content = commentsEntity.getContent();//评论内容
        Date createdAt = commentsEntity.getCreatedAt();// 评论时间
        AuthorEntity authorEntity = commentsEntity.getAuthor();
        String username = authorEntity.getUsername(); // 评论作者
        tvContent.setText(content);
        tvAuthor.setText(username);
        tvCreatedAt.setText(CommonUtils.format(createdAt));
    }
}
