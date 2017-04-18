package com.fuicuiedu.xc.videonew_20170309.bombapi.entity;

import com.fuicuiedu.xc.videonew_20170309.bombapi.other.AuthorPointer;
import com.fuicuiedu.xc.videonew_20170309.bombapi.other.NewsPointer;

/**
 *    发布评论的实体类
 */

//      {
//        "content":评论内容,————————————————用到
//        "author":{    ——————————————————————————对应other中的AuthorPointer
//        "__type":"Pointer",
//        "className":"_User",
//        "objectId":用户Id
//              }
//        "news":{——————————————————————————对应other中的PublishEntity
//        "__type":"Pointer",
//        "className":"News",
//        "objectId":新闻Id
//              }
//     }

public class PublishEntity {
    private String content;
    private AuthorPointer author;    //拿出来作为新的类
    private NewsPointer news;    //拿出来作为新的类

    public PublishEntity(String content, String userId, String newsId) {
        this.content = content;
        this.author = new AuthorPointer(userId);
        this.news = new NewsPointer(newsId);
    }
}
