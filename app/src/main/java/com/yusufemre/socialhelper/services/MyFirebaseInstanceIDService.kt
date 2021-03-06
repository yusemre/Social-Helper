package com.yusufemre.socialhelper.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        var refreshedToken:String? = FirebaseInstanceId.getInstance().token

        if(!refreshedToken.isNullOrEmpty()){
            tokenVeriTabaninaKaydet(refreshedToken)
        }

    }

    private fun tokenVeriTabaninaKaydet(refreshedToken: String?) {

        if(FirebaseAuth.getInstance().currentUser != null) {
            var ref = FirebaseDatabase.getInstance().reference
                    .child("kullanici")
                    .child(FirebaseAuth.getInstance().currentUser?.uid)
                    .child("mesaj_token")
                    .setValue(refreshedToken)
        }
    }
}