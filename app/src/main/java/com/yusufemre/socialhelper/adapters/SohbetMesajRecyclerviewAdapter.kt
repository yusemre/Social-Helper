package com.yusufemre.socialhelper.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.yusufemre.socialhelper.R
import com.yusufemre.socialhelper.model.SohbetMesaj
import kotlinx.android.synthetic.main.tek_satir_mesaj_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class SohbetMesajRecyclerviewAdapter(context: Context, tumMesajlar:ArrayList<SohbetMesaj>):RecyclerView.Adapter<SohbetMesajRecyclerviewAdapter.SohbetMesajViewHolder>() {

    var myContext=context
    var myTumMesajlar=tumMesajlar





    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SohbetMesajViewHolder {

        var inflater=LayoutInflater.from(myContext)



        var view:View? = null
        if(viewType==2)
        {
            view=inflater.inflate(R.layout.tek_satir_mesaj_layout , parent , false)
        }else {
            view=inflater.inflate(R.layout.tek_satir_mesaj_layout2 , parent , false)
        }


        return SohbetMesajViewHolder(view)
    }


    override fun onBindViewHolder(holder: SohbetMesajViewHolder?, position: Int) {

        var oanKiMesaj = myTumMesajlar.get(position)
        holder?.setData(oanKiMesaj, position)

    }

    override fun getItemViewType(position: Int): Int {
        if(myTumMesajlar.get(position).kullanici_id.equals(FirebaseAuth.getInstance().currentUser?.uid)){
            return 1
        }else {
            return 2
        }
    }

    override fun getItemCount(): Int {
        return myTumMesajlar.size
    }


    inner class SohbetMesajViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView){

        var tumLayout=itemView as ConstraintLayout
        //  var profilResmi=tumLayout.imgMesajprofilResmi
        var mesaj=tumLayout.tvMesaj
        var isim=tumLayout.tvMesajUserAd
        var tarih=tumLayout.tvMesajTarih



        fun setData(oanKiMesaj: SohbetMesaj, position: Int) {

            mesaj.text=oanKiMesaj.mesaj
            isim.text=oanKiMesaj.adi

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
            val parsedDate = format.parse(oanKiMesaj.timestamp)
            val timestamp = java.sql.Time(parsedDate.time)

            tarih.text=timestamp.toString()
/*
            if(!oanKiMesaj.profil_resmi.isNullOrEmpty()){
                Picasso.with(myContext).load(oanKiMesaj.profil_resmi).resize(48,48).into(profilResmi)
            }
*/


        }



    }



}