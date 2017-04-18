package com.fuicuiedu.xc.videonew_20170309.bombapi.entity;

/**
 *  用户的实体类（注册时的请求体）
 *   通过构造方法——存储用户名和密码
 */

public class UserEntity {

    private String username;
    private String password;

    //生成构造方法的快捷     alt + insert ->constructor
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
