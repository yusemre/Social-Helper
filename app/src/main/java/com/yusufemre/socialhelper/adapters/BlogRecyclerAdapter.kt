package com.yusufemre.socialhelper.adapters

/**
 * Created by yusem on 25.04.2018.
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.yusufemre.socialhelper.R
import com.yusufemre.socialhelper.model.Blog
import java.lang.Long
import java.text.DateFormat
import java.util.*

/**
 * Created by paulodichone on 4/17/17.
 */

class BlogRecyclerAdapter(private var context: Context?, private val blogList: List<Blog>) : RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_row, parent, false)


        return ViewHolder(view, context!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val blog = blogList[position]
        var imageUrl: String? = null

        holder.title.setText(blog.title)
        holder.desc.setText(blog.desc)



        val dateFormat = DateFormat.getDateInstance()
        val formattedDate = dateFormat.format(Date(Long.valueOf(blog.timestamp)).getTime())



        holder.timestamp.text = formattedDate

        imageUrl = blog.image


        //TODO: Use Picasso library to load image
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.image)


    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    inner class ViewHolder(view: View, ctx: Context) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var desc: TextView
        var timestamp: TextView
        var image: ImageView
        internal var userid: String? = null

        init {

            context = ctx

            title = view.findViewById(R.id.postTitleList) as TextView
            desc = view.findViewById(R.id.postTextList) as TextView
            image = view.findViewById(R.id.postImageList) as ImageView
            timestamp = view.findViewById(R.id.timestampList) as TextView

            userid = null

            view.setOnClickListener {
                // we can go to the next activity...
            }

        }
    }
}
