package com.fuicuiedu.xc.videonew_20170309.bombapi;

import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.UserEntity;
import com.fuicuiedu.xc.videonew_20170309.bombapi.result.UserResult;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 *   \ 用户登录和注册的相关网络接口
 */

public interface UserApi {

    //登录
    @GET("1/login")
    Call<UserResult> login(@Query("username") String username, @Query("password") String password);

    //注册
    @POST("1/users")
    Call<UserResult> register(@Body UserEntity userEntity);
}
