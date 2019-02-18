package com.yusufemre.socialhelper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.yusufemre.socialhelper.model.Post
import com.yusufemre.socialhelper.model.PostInfo
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.post_arayuz.view.*
import kotlinx.android.synthetic.main.post_gonder.*
import kotlinx.android.synthetic.main.post_gonder.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PostActivity : AppCompatActivity() {
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference
    private var mAuth:FirebaseAuth?=null

    var ListPosts= ArrayList<Post>()
    var adpater:MyTweetAdpater?=null
    var myname:String?=null
    var UserUID:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

/*
        var b: Bundle? =intent.extras
        myname= b?.getString("email")
        UserUID= b?.getString("uid") */
        //Dummy data
        myname = FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser?.uid)
                .child("isim").toString()


        ListPosts.add(Post("0","him","url","add"))


        adpater= MyTweetAdpater(this,ListPosts)
        lvTweets.adapter=adpater

        LoadPost()
    }
    inner class  MyTweetAdpater: BaseAdapter {
        var listNotesAdpater=ArrayList<Post>()
        var context: Context?=null
        constructor(context: Context, listNotesAdpater:ArrayList<Post>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {


            var mypost=listNotesAdpater[p0]

            if(mypost.postPersonUID.equals("add")) {
                var myView = layoutInflater.inflate(R.layout.post_gonder, null)

                myView.iv_attach.setOnClickListener(View.OnClickListener {
                   // loadImage()
                    checkPermission()

                })

                myView.iv_post.setOnClickListener(View.OnClickListener {
                    //upload server
                    /*
                    myRef.child("posts").push().child("UserUID").setValue(FirebaseAuth.getInstance().currentUser?.uid)
                    myRef.child("posts").push().child("text").setValue(myView.etPost.text.toString())
                    myRef.child("posts").push().child("postImage").setValue(DownloadURL)
                    myView.etPost.setText("")
                     */

                var yenipost = PostInfo()
                yenipost.UserUID=FirebaseAuth.getInstance().currentUser?.uid
                yenipost.text=myView.etPost.text.toString()
                yenipost?.postImage=DownloadURL
                myRef!!.child("posts").push().setValue(yenipost)
                    myView.etPost.setText("")
                    SaveImageInFirebase()

                    /*
                    myRef!!.child("posts").push().setValue(
                            PostInfo(UserUID!!,
                                    myView.etPost.text.toString(), DownloadURL!!))

                    myView.etPost.setText("")
                    */
                })
                return myView
            } /*else if(mytweet.postPersonUID.equals("loading")){
                var myView=layoutInflater.inflate(R.layout.loading_post,null)
                return myView
            } */else{
                var myView=layoutInflater.inflate(R.layout.post_arayuz,null)
                myView.txt_tweet.text = mypost.postText

                //myView.tweet_picture.setImageURI(mytweet.tweetImageURL)
                Picasso.with(this@PostActivity).load(R.drawable.ic_account_circle).into(myView.tweet_picture)


                myRef.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid)
                        .addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                                try {

                                    var td= dataSnapshot!!.value as HashMap<String,Any>

                                    for(key in td.keys){

                                        var userInfo= td[key] as String
                                        if(key.equals("PostImage")){
                                            Picasso.with(context).load(userInfo).into(myView.picture_path)
                                        }else{
                                            myView.txtUserName.text = userInfo
                                        }



                                    }

                                }catch (ex:Exception){}


                            }

                            override fun onCancelled(p0: DatabaseError?) {

                            }
                        })

                return myView
            }



        }



        override fun getItem(p0: Int): Any {
            return listNotesAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {

            return listNotesAdpater.size

        }



    }
    //Load image
/*
    val PICK_IMAGE_CODE=123
    fun loadImage(){

        var intent= Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }
*/
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= contentResolver.query(selectedImage,filePathColum,null,null,null)
            cursor.moveToFirst()
            val coulomIndex=cursor.getColumnIndex(filePathColum[0])
            val picturePath=cursor.getString(coulomIndex)
            cursor.close()
            UploadImage(BitmapFactory.decodeFile(picturePath))
        }

    } */
/*
    var DownloadURL:String?=""
    fun UploadImage(bitmap: Bitmap){
     //   ListPosts.add(0,Post("0","him","url","loading"))
       // adpater!!.notifyDataSetChanged()
        var currentUser =mAuth!!.currentUser

        val email:String=currentUser!!.email.toString()

        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://social-helper-1905.appspot.com")
        val df= SimpleDateFormat("yyyy-MM-dd", Locale("tr"))
        val dataobj= Date()
        val imagePath= SplitString(email) + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child("imagePost/" +imagePath )
        val baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()
        val uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"fail to upload", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->

                    DownloadURL= taskSnapshot.downloadUrl!!.toString()
                    ListPosts.removeAt(0)
                    adpater!!.notifyDataSetChanged()

                }
    }
    */


    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }



    fun LoadPost(){

        myRef.child("posts")
                .addValueEventListener(object :ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        try {

                            ListPosts.clear()
                            ListPosts.add(Post("0","him","url","add"))
                            //ListPosts.add(Post("0","him","url","ads"))
                            var td= dataSnapshot!!.value as HashMap<String,Any>

                            for(key in td.keys){

                                var post= td[key] as HashMap<String,Any>

                                ListPosts.add(Post(key,
                                        post["text"] as String,
                                        post["postImage"] as String
                                        ,post["userUID"] as String))


                            }


                            adpater!!.notifyDataSetChanged()
                        }catch (ex:Exception){}


                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }
                })
    }

    val READIMAGE:Int=253
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
                return
            }
        }

        loadImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }

    val PICK_IMAGE_CODE=123
    fun loadImage(){

        var intent=Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= contentResolver.query(selectedImage,filePathColum,null,null,null)
            cursor.moveToFirst()
            val coulomIndex=cursor.getColumnIndex(filePathColum[0])
            val picturePath=cursor.getString(coulomIndex)
            cursor.close()
            iv_attach.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }

    }
    var DownloadURL:String?=""
    fun SaveImageInFirebase(){
        var currentUser =mAuth?.currentUser
        var email:String=currentUser?.email.toString()
        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://social-helper-1905.appspot.com")
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val dataobj=Date()
        val imagePath= SplitString(email) + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child("imagepost/"+imagePath )
        iv_attach.isDrawingCacheEnabled=true
        iv_attach.buildDrawingCache()

        val drawable=iv_attach!!.drawable as BitmapDrawable?
        val bitmap=drawable?.bitmap
        val baos= ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()
        val uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"fail to upload",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->

                    //var DownloadURL= taskSnapshot.downloadUrl!!.toString()

                  //Todo register to database
                    //Log.d("DownloadURL", DownloadURL)
                    DownloadURL= taskSnapshot.downloadUrl!!.toString()
                    //ListPosts.removeAt(0)
                    adpater!!.notifyDataSetChanged()

                }

    }

}
