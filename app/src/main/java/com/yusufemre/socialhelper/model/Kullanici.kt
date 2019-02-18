package com.yusufemre.socialhelper.model


class Kullanici {

    var isim: String? = null
    var telefon: String? = null
    var profil_resmi: String? = null
    var seviye: String? = null
    var kullanici_id: String? = null
    var mesaj_token:String? = null
    var latitude: Double? = null
    var longitude:Double? = null



    constructor(isim: String, telefon: String, profil_resmi: String, seviye: String, kullanici_id: String, latitude: Double, longitude: Double) {
        this.isim = isim
        this.telefon = telefon
        this.profil_resmi = profil_resmi
        this.seviye = seviye
        this.kullanici_id = kullanici_id
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor() {}
}
