package com.fuicuiedu.xc.videonew_20170309;

/**
 *  很简单的用户管理类。
 */
public class UserManager {

    /**
     * 单例管理类usermanager
     */
    private UserManager(){}
    private static UserManager sInstance;
    public static UserManager getInstance(){
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }


    /**
     *  提供用户名和id的封装
     */
    private String username;
    private String objectId;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    /**
     * 判断当前的用户名和id是否为空
     * 有一个为空，就是空——————返回true——代码没有用户信息
     */
    public boolean isOffline(){
        return username == null || objectId == null;
    }

    /**
     * 清除账号的信息clear方法
     */
    public void clear(){
        username = null;
        objectId = null;
    }
}
