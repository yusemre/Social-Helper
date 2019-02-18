package com.yusufemre.socialhelper


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.yusufemre.socialhelper.adapters.BlogRecyclerAdapter
import com.yusufemre.socialhelper.model.Blog
import java.util.*


class PostListActivity : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    private var blogRecyclerAdapter: BlogRecyclerAdapter? = null
    private var blogList: MutableList<Blog>? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mUser: FirebaseUser? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("MBlog")
        mDatabaseReference!!.keepSynced(true)


        blogList = ArrayList<Blog>()


        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView?
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.anamenu!!, menu)
        //return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_add -> if (mUser != null && mAuth != null) {
                var intent = Intent(this,AddPostActivity::class.java)
                startActivity(intent)
                return true
                //finish()

            }
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

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        mDatabaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, s: String?) {

                val blog = dataSnapshot!!.getValue<Blog>(Blog::class.java)



                blogList!!.add(blog!!)

                Collections.reverse(blogList!!)

                blogRecyclerAdapter = BlogRecyclerAdapter(this@PostListActivity, blogList!!)
                recyclerView!!.adapter = blogRecyclerAdapter
                blogRecyclerAdapter!!.notifyDataSetChanged()


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }
    private fun cikisyap() {
        FirebaseAuth.getInstance().signOut()
    }
}
