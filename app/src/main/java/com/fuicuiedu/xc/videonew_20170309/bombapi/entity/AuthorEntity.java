package com.fuicuiedu.xc.videonew_20170309.bombapi.entity;

import com.google.gson.annotations.SerializedName;

/**
 *   好多gson数据所共有的部分字段
 *   单独拿出来，做一个类
 */

//        "author": {
//        "__type": "Pointer",
//        "className": "_User",
//        "objectId": "79e992125d"
//        }

    public class AuthorEntity {


    /**使用注解，感觉__type，作为对象名不是很好*/
    @SerializedName("__type")
    private String type;
    private String objectId;
    private String username;


    public String getType() {
        return type;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getUsername() {
        return username;
    }
}
