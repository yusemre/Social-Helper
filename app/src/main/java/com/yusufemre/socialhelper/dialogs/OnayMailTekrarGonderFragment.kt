package com.yusufemre.socialhelper.dialogs



import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.yusufemre.socialhelper.R


class OnayMailTekrarGonderFragment : DialogFragment() {

    lateinit var emailEditText:EditText
    lateinit var sifreEditText: EditText
    lateinit var mContext:FragmentActivity



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_onay_mail_tekrar_gonder, container, false)

        emailEditText = view.findViewById(R.id.etDialogMail)
        sifreEditText = view.findViewById(R.id.etDialogSifre)
        mContext = activity

        var btnIptal = view.findViewById<Button>(R.id.btnDiaolgIptal)
        btnIptal.setOnClickListener {
            dialog.dismiss()
        }


        var btnGonder = view.findViewById<Button>(R.id.btnDialogGonder)

        btnGonder.setOnClickListener {

            if(emailEditText.text.toString().isNotEmpty() && sifreEditText.text.toString().isNotEmpty()){

                girisYapveOnayMailiniTekrarGonder(emailEditText.text.toString(), sifreEditText.text.toString())


            }
            else{
                Toast.makeText(mContext,"Boş alanları doldurunuz",Toast.LENGTH_SHORT).show()
            }





            // Toast.makeText(activity,"Gonder Tıklandı",Toast.LENGTH_SHORT).show()
        }


        return view
    }

    private fun girisYapveOnayMailiniTekrarGonder(email: String, sifre: String) {

        var credential = EmailAuthProvider.getCredential(email,sifre)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        onayMailiniTekrarGonder()
                        dialog.dismiss()

                    }else {
                        Toast.makeText(activity,"Email veya şifre hatalı.",Toast.LENGTH_SHORT).show()

                    }


                }
    }

    private fun onayMailiniTekrarGonder() {
        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici!=null){

            kullanici.sendEmailVerification()
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {

                            if(p0.isSuccessful) {
                                Toast.makeText(mContext, "Mail hesabınızı kontrol edin, onay maili gönderildi.",
                                        Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(mContext, "Onay maili gönderilemedi." +p0.exception?.message,
                                        Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
        }
        /* else{
            Toast.makeText(mContext, "Bilgilerinizi kontrol edin.",
                    Toast.LENGTH_SHORT).show()

        } */
    }

}
