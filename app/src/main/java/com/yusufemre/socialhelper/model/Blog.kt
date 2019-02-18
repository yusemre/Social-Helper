package com.yusufemre.socialhelper.model

/**
 * Created by yusem on 25.04.2018.
 */

class Blog {
    var title: String? = null
    var desc: String? = null
    var image: String? = null
    var timestamp: String? = null
    var userid: String? = null

    constructor() {}

    constructor(title: String, desc: String, image: String, timestamp: String, userid: String) {
        this.title = title
        this.desc = desc
        this.image = image
        this.timestamp = timestamp
        this.userid = userid
    }
}
