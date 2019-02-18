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
import com.google.firebase.auth.FirebaseAuth
import com.yusufemre.socialhelper.R
import kotlinx.android.synthetic.main.fragment_sifremi_unuttum_dailog.*


class SifremiUnuttumDailogFragment : DialogFragment() {

    lateinit var emailEditText:EditText
    lateinit var mContext: FragmentActivity

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_sifremi_unuttum_dailog, container, false)

        emailEditText = view.findViewById(R.id.etSifreyiTekrarGonder)

        mContext = activity


        var btnİptal = view.findViewById<Button>(R.id.btnSifreyiUnuttumİptal)
        btnİptal.setOnClickListener {
            dialog.dismiss()
        }

        var btnGonder = view.findViewById<Button>(R.id.btnSifreyiUnuttumGonder)
        btnGonder.setOnClickListener {

            FirebaseAuth.getInstance().sendPasswordResetEmail(etSifreyiTekrarGonder.text.toString())
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(activity,"Şifre sıfırlama maili gönderildi.", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()

                        }else {
                            Toast.makeText(activity,"Hata oluştu." +task.exception?.message, Toast.LENGTH_SHORT).show()

                        }
                    }

        }


        return view
    }

}
