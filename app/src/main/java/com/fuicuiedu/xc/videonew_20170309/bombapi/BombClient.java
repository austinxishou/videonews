package com.fuicuiedu.xc.videonew_20170309.bombapi;

import com.fuicuiedu.xc.videonew_20170309.bombapi.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BombClient {

    /** ohhttpclient和retrofit*/
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;


    /**用户和新闻的api*/
    private UserApi userApi;
    private NewsApi newsApi;

    /**新接口*/
    private Retrofit retrofit_cloud;
    private NewsApi newsApi_cloud;


    /**————————————————————————————————————————————————————————————————————————
     * 单例bombClient
     * 私有构造方法
     * 私有静态的成员变量
     * 共有静态的带类返回类型的放大
     */
    private static BombClient bombClient;
    public static BombClient getInstance(){
        if (bombClient == null){
            bombClient = new BombClient();
        }
        return bombClient;
    }
    private BombClient(){
        /**构建“日志拦截器”——并设置拦截器的级别*/
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        /**其他的写法*/
//        okHttpClient = new OkHttpClient();
//        okHttpClient.interceptors().add(httpLoggingInterceptor);
        /**将拦截器绑定在okhttpclient上*/
      okHttpClient = new OkHttpClient.Builder()
                //添加Bomb必要的头字段的拦截器
                .addInterceptor(new BombIntercepted())
                //添加日志拦截器
                .addInterceptor(httpLoggingInterceptor)
                .build();

        /**让gson能够将bomb返回的时间戳自动转换为Date对象
         * 这样实体类中才可以，直接将时间的类型写成data类型
         * ————————————————————————————————————————————————————————————*/
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        /**创建retrofit对象，构造器模式
         * 目的是使用okhttpclient身上的拦截器————因为retrofit本身没有拦截器
         * */
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.bmob.cn/")
                /**添加转换器——————————作用是*/
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        /**构建retrofit_cloud*/
        retrofit_cloud = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://cloud.bmob.cn/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**拿到UserApi————————create创建retrofit需要用到接口类*/
    public UserApi getUserApi(){
        if (userApi == null){
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

    /**拿到NewsApi*/
    public NewsApi getNewsApi(){
        if (newsApi == null){
            /**retrofit创建，需要传入接口的类*/
            newsApi = retrofit.create(NewsApi.class);
        }
        return newsApi;
    }

    /**拿到newsApi_cloud*/
    public NewsApi getNewsApi_cloud(){
        if (newsApi_cloud == null){
            newsApi_cloud = retrofit_cloud.create(NewsApi.class);
        }
        return newsApi_cloud;
    }

   /**注册时候不适用retrofit的转换器
    * 先将存用户名和密码的实体类转换成json格式的数据
    * 将json数据放到请求体requestbody中，当做request中post的请求体。
    * */
    public Call register(String username, String password){
        //构建一个请求的请求体（根据服务器要求）
        //Gson 是一个用来生成，解析json数据的第三方库
        //生成，可以将一个类，生成为一串json格式的数据
        String json = new Gson().toJson(new UserEntity(username,password));

        RequestBody requestBody = RequestBody.create(null,json);

        Request request = new Request.Builder()
                .url("https://api.bmob.cn/1/users")
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

     /**登录的操作——如果不用retrofit的话，登录时候get一下，需要拼接起来username和password*/
    public Call login(String username,String password){
        Request request = new Request.Builder()
                .get()
                .url("https://api.bmob.cn/1/login" + "?"
                        +"username=" + username + "&"
                        +"password=" + password)
                .build();
        return okHttpClient.newCall(request);
    }
}
