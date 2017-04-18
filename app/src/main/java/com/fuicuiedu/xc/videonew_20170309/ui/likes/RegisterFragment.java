package com.fuicuiedu.xc.videonew_20170309.ui.likes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import com.fuicuiedu.xc.videonew_20170309.R;
import com.fuicuiedu.xc.videonew_20170309.bombapi.BombClient;
import com.fuicuiedu.xc.videonew_20170309.bombapi.UserApi;
import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.UserEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.ErrorResult;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.UserResult;
import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Cre ated by Administrator on 2016/12/21 0021.
 */

public class RegisterFragment extends DialogFragment {


    /**用户名和密码的edittext和注册按钮*/
    @BindView(R.id.etUsername)
    EditText mEtUsername;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.chenghao)
    EditText mchenghao;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**取消标题栏*/
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        /**填充布局和butterknife*/
        View view = inflater.inflate(R.layout.dialog_register,container,false);
        ButterKnife.bind(this,view);
        return view;
    }


    /**注册按钮点击的监听*/
    @OnClick(R.id.btnRegister)
    public void onClick(){
        final String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();

     /**用户名和密码不能为空*/
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            ToastUtils.showShort(R.string.username_or_password_can_not_be_null);
            return;
        }

        /**网络模块，注册的post请求————注册的api*/
        UserApi userApi = BombClient.getInstance().getUserApi();
        /**构建用户实体类——通过构造方法将用户名和密码存到实体类中*/
        UserEntity userEntity = new UserEntity(username,password);
        /**拿到call模型——userapi中的register注册方法,会query查询实体类中的username和password（因为转换器——可以传入实体类userEntity）*/
        Call<UserResult> call = userApi.register(userEntity);
        /**执行网络请求——异步请求enqueue——规定返回类型是userresult*/
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                /**响应中——隐藏加载圈圈*/
                mBtnRegister.setVisibility(View.VISIBLE);
                /**注册失败*/
                if (!response.isSuccessful()){
                    try {
                        /**拿到失败的json*/
                        String error = response.errorBody().string();
                        /**通过gson将拿到的json数据解析成失败结果类*/
                        ErrorResult errorResult = new Gson().fromJson(error,ErrorResult.class);
                        /**提示用户注册失败*/
                        ToastUtils.showShort(errorResult.getError());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                /**注册成功——userresult转化成实体类userresult*/
                UserResult userResult = response.body();
                /**注册成功后的逻辑——写成接口形式（mvp模式，将试图的操作放到likefragment中执行）*/
                listener.registerSuccess(username,userResult.getObjectId());
                /**提示注册成功*/
                ToastUtils.showShort(R.string.register_success);
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                /**注册失败——隐藏圈圈——提示失败原因*/
                mBtnRegister.setVisibility(View.VISIBLE);
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    /**当注册成功会触发的接口*/
    public interface OnRegisterSuccessListener{
        /** 当注册成功时，来调用*/
        void registerSuccess(String username,String objectId);
    }

    private OnRegisterSuccessListener listener;

    public void setListener(OnRegisterSuccessListener listener){
        this.listener = listener;
    }

}
