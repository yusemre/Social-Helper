package com.yusufemre.socialhelper.model

/**
 * Created by yusem on 17.04.2018.
 */
class Post {
    var postID:String?=null
    var postText:String?=null
    var postImageURL:String?=null
    var postPersonUID:String?=null
    constructor(postID:String,postText:String,postImageURL:String,postPersonUID:String){
        this.postID=postID
        this.postText=postText
        this.postImageURL=postImageURL
        this.postPersonUID=postPersonUID
    }

}