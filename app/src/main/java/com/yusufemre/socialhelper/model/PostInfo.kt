package com.yusufemre.socialhelper.model

/**
 * Created by yusem on 17.04.2018.
 */
class PostInfo {
    var UserUID:String?=null
    var text:String?=null
    var postImage:String?=null
    constructor(UserUID:String,text:String,postImage:String){
        this.UserUID=UserUID
        this.text=text
        this.postImage=postImage
    }
    constructor() {}
}
