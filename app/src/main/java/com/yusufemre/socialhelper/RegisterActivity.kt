package com.yusufemre.socialhelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.yusufemre.socialhelper.model.Kullanici
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnKayit.setOnClickListener {

            if ( etMail.text.isNotEmpty() && etSifre.text.isNotEmpty() && etSifreTekrar.text.isNotEmpty()){
                if(etSifre.text.toString().equals(etSifreTekrar.text.toString())){
                    progresbarGoster()
                    yeniUyeKayit(etMail.text.toString(),etSifre.text.toString())
                }
                else {
                    Toast.makeText(this, "Girdiğiniz Şifreler Birbiri İle Uyuşmadı.", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this,"Belirtilen Alanları Eksik Girdiniz. Tekrar Deneyin", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun yeniUyeKayit(mail: String, sifre: String) {
        progresbarGoster()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,sifre)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if(p0.isSuccessful){
                            progresbarGizle()
                           /* Toast.makeText(this@RegisterActivity, "Kullanıcı Kaydı Gerçekleştirildi."
                                    + FirebaseAuth.getInstance().currentUser?.uid , Toast.LENGTH_SHORT).show() */
                            onayMailiGonder()


                            var veritabaninaEklenecekKullanici= Kullanici()
                            veritabaninaEklenecekKullanici.isim=etMail.text.toString().substring(0,etMail.text.toString().indexOf("@"))
                            veritabaninaEklenecekKullanici.kullanici_id=FirebaseAuth.getInstance().currentUser?.uid
                            veritabaninaEklenecekKullanici.profil_resmi="https://i.hizliresim.com/BLggE9.jpg"
                            veritabaninaEklenecekKullanici.telefon="123"
                            veritabaninaEklenecekKullanici.latitude=39.932901
                            veritabaninaEklenecekKullanici.longitude=32.846412
                            veritabaninaEklenecekKullanici.seviye="1"


                            FirebaseDatabase.getInstance().reference
                                    .child("kullanici")
                                    .child(FirebaseAuth.getInstance().currentUser?.uid)
                                    .setValue(veritabaninaEklenecekKullanici).addOnCompleteListener { task->

                                if(task.isSuccessful){
                                    Toast.makeText(this@RegisterActivity, "Üye kaydedildi:" + FirebaseAuth.getInstance().currentUser?.uid, Toast.LENGTH_SHORT).show()
                                    FirebaseAuth.getInstance().signOut()
                                    loginSayfasinaYonlendir()
                                }

                            }


                            // FirebaseAuth.getInstance().signOut()
                           // reDirectLoginPage()
                        }
                        else {
                            progresbarGizle()

                            Toast.makeText(this@RegisterActivity, "Kayıt gerçekleştirilirlen bir hata oluştu." +p0.exception?.message,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }

                })

    }

    private fun onayMailiGonder(){

        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici!=null){

            kullanici?.sendEmailVerification()
                    ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {

                            if(p0.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Mail hesabınızı kontrol edin, onay maili gönderildi.",
                                        Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(this@RegisterActivity, "Onay maili gönderilemedi." +p0.exception?.message,
                                        Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
        }
        /* else{
             Toast.makeText(this@RegisterActivity, "Bilgilerinizi kontrol edin.",
                     Toast.LENGTH_SHORT).show()

         } */

    }

    private fun progresbarGoster(){
        progressBar.visibility = View.VISIBLE

    }
    private fun progresbarGizle(){
        progressBar.visibility = View.INVISIBLE

    }
    fun reDirectLoginPage(){
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun loginSayfasinaYonlendir() {

        var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
