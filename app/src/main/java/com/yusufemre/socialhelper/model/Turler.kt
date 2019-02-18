package com.yusufemre.socialhelper.model

/**
 * Created by yusem on 21.03.2018.
 */

class Turler {

    var turResim: Int? = null
    var kategori: String? = null
    var latitude: Double? = null
    var longitude: Double? = null

    constructor() {}

    constructor( turResim: Int, kategori: String, latitude: Double?, longitude: Double?) {
        this.turResim = turResim
        this.kategori = kategori
        this.latitude = latitude
        this.longitude = longitude

    }


}
