package com.fuicuiedu.xc.videonew_20170309.bombapi.entity;

import java.util.Date;

/**
 *   评论的实体类
 *   需要用到authorentity这个jsonobject
 */

//{
//        "author": {————————————————————用到
//        "__type": "Pointer",
//        "className": "_User",
//        "objectId": "79e992125d"
//        },


//        "content": "吃葡萄不吐葡萄皮",————————————————————用到
//        "createdAt": "2016-07-18 15:29:04",————————————————————用到


//        "news": {
//        "__type": "Pointer",
//        "className": "News",
//        "objectId": "IPPmF99F"
//        },



//        "objectId": "26ec97caa4",————————————————————用到
//        "updatedAt": "2016-07-18 15:29:04"
//        }

public class CommentsEntity {

    private String objectId;
    private String content;
    private AuthorEntity author;    /**共有的拿出去当做类使用————author字段返回的是AuthorEntity类*/
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getContent() {
        return content;
    }

    public AuthorEntity getAuthor() {
        return author;
    }
}
