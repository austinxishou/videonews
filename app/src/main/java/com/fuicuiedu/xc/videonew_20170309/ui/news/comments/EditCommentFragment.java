package com.fuicuiedu.xc.videonew_20170309.ui.news.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.UserManager;
import com.fuicuiedu.xc.videonew_20170309.bombapi.BombClient;
import com.fuicuiedu.xc.videonew_20170309.bombapi.NewsApi;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.PublishEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.CommentsResult;
import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCommentFragment extends DialogFragment {

    /**私有的静态常量值*/
    private static final String KEY_NEWS_ID = "KEY_NEWS_ID";


    /**对外界提供一个实例化本dialogfragment的方法，但是需要传入新闻的id
     * 用的是activity传值给fragment的方法
     * */
    public static EditCommentFragment getInstance(String newsId){
        EditCommentFragment fragment = new EditCommentFragment();
        /**通过bundle传入新闻的id*/
        Bundle args = new Bundle();
        args.putString(KEY_NEWS_ID, newsId);
        /**将bundle对象传入setargument的参数中*/
        fragment.setArguments(args);
        return fragment;
    }

    /**butterknife的绑定*/
    private Unbinder unbinder;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btnOK)
    Button btnOk;

    /**oncreat方法中——取消标题栏和填充布局*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**无标题栏*/
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_edit_comment, container, false);
    }

    /**viewcreat方法中，绑定控件*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnOK)
    public void postComment(){
        /**从edittext获取评论内容，且不能为空*/
        String comment = etComment.getText().toString();
        if(TextUtils.isEmpty(comment)){
            ToastUtils.showShort(R.string.please_edit_comment);
            return;
        }
        /**用了framelayout，按钮隐藏时候，后面的progressbar就会显示出来哦*/
        btnOk.setVisibility(View.INVISIBLE);
        /**评论处理——从usetmanager中获取用户的id，从argument中获取新闻的id*/
        String userId = UserManager.getInstance().getObjectId();
        String newsId = getArguments().getString(KEY_NEWS_ID);

        /**评论的网络请求——拿到newsapi对象*/
        NewsApi newsApi = BombClient.getInstance().getNewsApi();
        /**创建好需要上传的评论实体类（体）————评论内容和新闻id和用户id*/
        PublishEntity publishEntity = new PublishEntity(comment,userId,newsId);
        /**创建好发表评论用到的call，接收newsapi返回的call————在enqueue异步执行（内部类的形式）*/
        Call<CommentsResult> call = newsApi.postComments(publishEntity);
        call.enqueue(callback);
    }

    private Callback<CommentsResult> callback = new Callback<CommentsResult>() {
        @Override
        public void onResponse(Call<CommentsResult> call, Response<CommentsResult> response) {
            /**评论，得到相应后，隐藏按钮，后面的progress便显示出来*/
            btnOk.setVisibility(View.VISIBLE);
            /**响应成功的话，实现接听的接口，通知评论的activity更新ui*/
            if (response.isSuccessful()){
                listener.onCommentSuccess();
                return;
            }
            ToastUtils.showShort("评论异常");
        }

        @Override
        public void onFailure(Call<CommentsResult> call, Throwable t) {
            /***/
            btnOk.setVisibility(View.VISIBLE);
            ToastUtils.showShort(t.getMessage());
        }
    };

/**____________________________________________________________________________________________________
 * 自定义接口
 * 接口————提供评论发表成功之后的ui变更
 * 私有化的成员变量
 * 向外界提供传入对应接口的共有方法————将外界的接口传入进来
 * */
    /** 评论成功监听器*/
    public interface OnCommentSuccessListener{
        void onCommentSuccess();
    }
    private OnCommentSuccessListener listener;
    public void setListener(@NonNull OnCommentSuccessListener listener) {
        this.listener = listener;
    }

}
