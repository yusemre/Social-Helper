package com.yusufemre.socialhelper

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private var mPostImage: ImageButton? = null
    private var mPostTitle: EditText? = null
    private var mPostDesc: EditText? = null
    private var mSubmitButton: Button? = null
    private var mStorage: StorageReference? = null
    private var mPostDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var mProgress: ProgressDialog? = null
    private var mImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        mProgress = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser
        mStorage = FirebaseStorage.getInstance().reference


        mPostDatabase = FirebaseDatabase.getInstance().reference.child("MBlog")

        mPostImage = findViewById<View>(R.id.imageButton) as ImageButton
        mPostTitle = findViewById<View>(R.id.postTitleEt) as EditText
        mPostDesc = findViewById<View>(R.id.descriptionEt) as EditText
        mSubmitButton = findViewById<View>(R.id.submitPost) as Button

        mPostImage!!.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_CODE)
        }


        mSubmitButton!!.setOnClickListener {
            //Posting to our database
            if(mPostImage == null){
                Toast.makeText(this@AddPostActivity, "Lütfen Bir Resim Seçiniz", Toast.LENGTH_SHORT).show()
            }
            startPosting()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            mImageUri = data.data
            mPostImage!!.setImageURI(mImageUri)


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


    private fun startPosting() {

        mProgress!!.setMessage("Blog başlatılıyor...")
        mProgress!!.show()

        val titleVal = mPostTitle!!.text.toString().trim { it <= ' ' }
        val descVal = mPostDesc!!.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal)
                && mImageUri != null) {
            //start the uploading...
            //mImageUri.getLastPathSegment() == /image/myphoto.jpeg"

            val filepath = mStorage!!.child("MBlog_images").child(mImageUri!!.lastPathSegment)
            filepath.putFile(mImageUri!!).addOnSuccessListener { taskSnapshot ->
                val downloadurl = taskSnapshot.downloadUrl

                val newPost = mPostDatabase!!.push()


                val dataToSave = HashMap<String, String>()
                dataToSave["title"] = titleVal
                dataToSave["desc"] = descVal
                dataToSave["image"] = downloadurl!!.toString()
                dataToSave["timestamp"] = java.lang.System.currentTimeMillis().toString()
                dataToSave["userid"] = mUser!!.uid
                dataToSave["username"] = mUser!!.email!!

                newPost.setValue(dataToSave)


                //Old way
                //                    newPost.child("title").setValue(titleVal);
                //                    newPost.child("desc").setValue(descrVal);
                //                    newPost.child("image").setValue(downloadUrl.toString());
                //                    newPost.child("timestamp").setValue(java.lang.System.currentTimeMillis());

                mProgress!!.dismiss()

                startActivity(Intent(this@AddPostActivity, PostListActivity::class.java))
                finish()
            }


        }
    }

    companion object {
        private val GALLERY_CODE = 1
    }
}
