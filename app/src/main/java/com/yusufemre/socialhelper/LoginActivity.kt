package com.yusufemre.socialhelper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.yusufemre.socialhelper.dialogs.OnayMailTekrarGonderFragment
import com.yusufemre.socialhelper.dialogs.SifremiUnuttumDailogFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuthStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initMyAuthStateListener()

        tvKayit.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }

        tvOnayMailiGonder.setOnClickListener {
            var dialogGoster = OnayMailTekrarGonderFragment()
            dialogGoster.show(supportFragmentManager,"gosterdialog")
        }

        tvSifreUnut.setOnClickListener {
            var dialogSifreyiTekrarGonder = SifremiUnuttumDailogFragment()
            dialogSifreyiTekrarGonder.show(supportFragmentManager,"gosterdialog")
        }

        btnGirisYap.setOnClickListener {

            if (etMail.text.isNotEmpty() && etSifre.text.isNotEmpty()) {
                progresbarGoster()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(etMail.text.toString(), etSifre.text.toString())
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                            override fun onComplete(p0: Task<AuthResult>) {
                                if (p0.isSuccessful) {

                                    if(!p0.result.user.isEmailVerified){
                                        FirebaseAuth.getInstance().signOut()

                                    }
                                    progresbarGizle()
                                    //  Toast.makeText(this@LoginActivity, "Başarılı Giriş " + FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()
                                } else {
                                    progresbarGizle()
                                    Toast.makeText(this@LoginActivity, "Hatalı Giriş " + p0.exception?.message, Toast.LENGTH_SHORT).show()
                                }
                            }


                        })


            } else {
                Toast.makeText(this@LoginActivity, "Boş Alanları Doldurunuz", Toast.LENGTH_SHORT).show()

            }

        }



    }

    private fun progresbarGoster() {
        progressBarLogin.visibility = View.VISIBLE

    }

    private fun progresbarGizle() {
        progressBarLogin.visibility = View.INVISIBLE

    }

    private fun initMyAuthStateListener(){

        mAuthStateListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici = p0.currentUser

                if(kullanici != null){
                    if(kullanici.isEmailVerified){
                        Toast.makeText(this@LoginActivity, "Mail onaylanmış giriş yapılabilir.", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this@LoginActivity, MainActivity :: class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this@LoginActivity, "Mail adresinizi onaylayınız. ", Toast.LENGTH_SHORT).show()
                        // FirebaseAuth.getInstance().signOut()

                    }

                }
            }

        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
    }
}

