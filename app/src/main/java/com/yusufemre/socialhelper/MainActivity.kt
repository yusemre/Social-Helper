package com.yusufemre.socialhelper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    lateinit var myAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        initAuthStateListener()
        setKullaniciBilgileri()
        initFCM()
        getPendingIntent()



    }


    private fun getPendingIntent() {
        var gelenIntent = intent

        if (gelenIntent.hasExtra("sohbet_odasi_id")) {

            var intent = Intent(this, SohbetOdasiActivity::class.java)
            intent.putExtra("sohbet_odasi_id", gelenIntent.getStringExtra("sohbet_odasi_id"))
            startActivity(intent)


        }
    }

    private fun initFCM() {
        var token = FirebaseInstanceId.getInstance().token
        tokenVeriTabaninaKaydet(token)
    }

    private fun tokenVeriTabaninaKaydet(refreshedToken: String?) {

        var ref = FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser?.uid)
                .child("mesaj_token")
                .setValue(refreshedToken)
    }



    private fun setKullaniciBilgileri() {
        var kullanici = FirebaseAuth.getInstance().currentUser
        if (kullanici!=null){
            tvKullaniciAdi.text = if(kullanici.displayName.isNullOrEmpty()) "Tanımlanmadı" else kullanici.displayName
            tvKullaniciEmail.text = kullanici.email
            tvKullaniciUid.text = kullanici.uid

        }
    }

    private fun initAuthStateListener() {
        myAuthStateListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici = p0.currentUser
                if(kullanici != null){


                }else{
                    var intent = Intent(this@MainActivity,LoginActivity ::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )

                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.anamenu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            R.id.menuCikisYap ->{
                cikisyap()
                return true
            }

            R.id.menuHesapAyarlari ->{
                var intent = Intent(this,KullaniciAyarlariActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menuHarita ->{
                var intent = Intent(this,MapsActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menuSohbet ->{
                var intent = Intent(this,SohbetActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.durum_paylas ->{
                var intent = Intent(this,PostListActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.anket ->{
                var intent = Intent(this,SimpleActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.action_add ->{
                var intent = Intent(this,PostListActivity::class.java)
                startActivity(intent)
                return true

            }


        }


        return super.onOptionsItemSelected(item)
    }

    private fun cikisyap() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun onResume() {
        super.onResume()
        kullaniciyiKontrolEt()
    }

    private fun kullaniciyiKontrolEt() {
        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici == null) {
            var intent = Intent(this@MainActivity,LoginActivity ::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )

            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        if(myAuthStateListener != null){

            FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener)
        }
    }
}