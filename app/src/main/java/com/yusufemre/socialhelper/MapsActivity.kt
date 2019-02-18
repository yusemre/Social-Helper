package com.yusufemre.socialhelper

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yusufemre.socialhelper.model.LocationData
import com.yusufemre.socialhelper.model.Turler
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {




    private var currentLocationMaker: Marker? = null
    private var currentLocationLatLong: LatLng? = null
    private var mDatabase: DatabaseReference? = null



    private lateinit var mMap: GoogleMap
    // var kullanici = FirebaseAuth.getInstance().currentUser!!

    var konumId: String = ""

    var key = FirebaseDatabase.getInstance().getReference()
            .child("turler").push().key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        LoadKategori()
        checkPermmison()
        //konumlariOku()
        //getMarkers()


    }

    private fun konumlariOku() {

        var mapreferans = FirebaseDatabase.getInstance().getReference()
        var sorgu = mapreferans?.child("turler")!!
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        for (singleSnapshot in p0!!.children) {
                            var turlerGetir = Turler()
                            var latitudeKonum = turlerGetir?.latitude
                            var longitudeKonum = turlerGetir?.longitude



                            val konumGetir = LatLng(latitudeKonum!!, longitudeKonum!!)
                            mMap!!.addMarker(MarkerOptions()
                                    .position(konumGetir)
                                    .title("Yardım")
                                    .snippet("Yardımın Bulunduğu Konum" +latitudeKonum!!+"," +longitudeKonum!!)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))


                        }
                    }

                })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mapsmenu,menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {


            R.id.menu_addmarkers -> {
                addMarker()
                return true
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
        }


        return super.onOptionsItemSelected(item)
    }

    private fun cikisyap() {
        FirebaseAuth.getInstance().signOut()
    }

    fun addMarker() {
        if(mMap != null){

            val layout = LinearLayout(this@MapsActivity)
            layout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            layout.orientation = LinearLayout.VERTICAL

            val titleField = EditText(this@MapsActivity)
            titleField.setHint("Başlık")

            val latField = EditText(this@MapsActivity)
            latField.setHint("Latitude")
            latField.setInputType(InputType.TYPE_CLASS_NUMBER
                    or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    or InputType.TYPE_NUMBER_FLAG_SIGNED)

            val lonField = EditText(this@MapsActivity)
            lonField.setHint("Longitude")
            lonField.setInputType(InputType.TYPE_CLASS_NUMBER
                    or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    or InputType.TYPE_NUMBER_FLAG_SIGNED)

            layout.addView(titleField)
            layout.addView(latField)
            layout.addView(lonField)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konum Ekle")
            builder.setView(layout)
            val alertDialog = builder.create()

            builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    var parsable:Boolean? = true
                    var lat: Double? = null
                    var lon: Double? = null

                    val strLat = latField.getText().toString()
                    val strLon = lonField.getText().toString()
                    val strTitle = titleField.getText().toString()

                    try {
                        lat = java.lang.Double.parseDouble(strLat)
                    } catch (ex: NumberFormatException) {
                        parsable = false
                        Toast.makeText(this@MapsActivity,
                                "Latitude değeri doğru değil",
                                Toast.LENGTH_LONG).show()
                    }

                    try {
                        lon = java.lang.Double.parseDouble(strLon)
                    } catch (ex: NumberFormatException) {
                        parsable = false
                        Toast.makeText(this@MapsActivity,
                                "Longitude değeri doğru değil",
                                Toast.LENGTH_LONG).show()
                    }

                    if (parsable!!) {

                        val targetLatLng = LatLng(lat!!.toDouble(), lon!!.toDouble())
                        val markerOptions = MarkerOptions().position(targetLatLng).title(strTitle)

                        mMap.addMarker(markerOptions)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(targetLatLng))

                    }
                }
            })
            builder.setNegativeButton("Cancel", null)

            builder.show()
        }
        else
        {
            Toast.makeText(this@MapsActivity, "Harita Hazır Değil", Toast.LENGTH_LONG).show()
        }
    }
    var ACCESSLOCATION=123
    private fun checkPermmison() {
        if(Build.VERSION.SDK_INT>=23){

            if(ActivityCompat.
                            checkSelfPermission(this,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATION)
                return
            }
        }

        GetUserLocation()

    }

    private fun GetUserLocation() {
        Toast.makeText(this,"User location access on", Toast.LENGTH_LONG).show()
        //TODO: Will implement later

        var myLocation= MylocationListener()

        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)


        var mythread=myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){

            ACCESSLOCATION->{

                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }else{
                    Toast.makeText(this,"Konumunuza Erişilmiyor.",Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near konum, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);




        /* // Add a marker in konum and move the camera
         val konum = LatLng(-34.0, 151.0)
         mMap.addMarker(MarkerOptions().position(konum).title("Marker in konum"))
         mMap.moveCamera(CameraUpdateFactory.newLatLng(konum)) */
    }
    var location: Location?=null

    //Get user location

    inner class MylocationListener: LocationListener {


        constructor(){
            location= Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location=p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        }

        override fun onProviderEnabled(p0: String?) {

        }

        override fun onProviderDisabled(p0: String?) {

        }

    }
    var oldLocation:Location?=null

    inner class myThread:Thread{

        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run(){

            while (true){

                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation=location


                    runOnUiThread {


                        mMap!!.clear()

                        // beni göster
                        val konum = LatLng(location!!.latitude, location!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                                .position(konum)
                                .title("Konumum")
                                .snippet("Bulunduğum Konum")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(konum, 14f))


                        for(i in 0..listkategori.size-1){

                            var newPockemon=listkategori[i]



                            val pockemonLoc = LatLng(newPockemon.latitude!!, newPockemon.longitude!!)
                            mMap!!.addMarker(MarkerOptions()
                                    .position(pockemonLoc)
                                    .title(newPockemon.kategori!!)
                                    .snippet("Yardımın Bulunduğu Konum" +newPockemon.latitude!! +"," +newPockemon.longitude!!)
                                    .icon(BitmapDescriptorFactory.fromResource(newPockemon.turResim!!)))


                        }






                        //   kullanici = FirebaseAuth.getInstance().currentUser!!


                        FirebaseDatabase.getInstance().reference
                                .child("kullanici")
                                .child(FirebaseAuth.getInstance().currentUser?.uid)
                                .child("latitude")
                                .setValue(location!!.latitude)
                        FirebaseDatabase.getInstance().reference
                                .child("kullanici")
                                .child(FirebaseAuth.getInstance().currentUser?.uid)
                                .child("longitude")
                                .setValue(location!!.longitude)

/*
                        var latitudeKonum:Double?=null
                        var longitudeKonum:Double?=null
                        var ref=FirebaseDatabase.getInstance().reference
                                .child("turler")
                                .orderByValue()
                                .addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onCancelled(p0: DatabaseError?) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot?) {


                                            latitudeKonum = p0!!.getValue(Turler::class.java)!!.latitude
                                            longitudeKonum = p0!!.getValue(Turler::class.java)!!.longitude


                                            val konumGetir = LatLng(latitudeKonum!!, longitudeKonum!!)
                                            mMap!!.addMarker(MarkerOptions()
                                                    .position(konumGetir)
                                                    .title("Yardım")
                                                    .snippet("Yardımın Bulunduğu Konum" + latitudeKonum!! + "," + longitudeKonum!!)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))



                                    }

                                })

                                */

                        var ref1=FirebaseDatabase.getInstance().reference
                                .child("turler1").addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onCancelled(p0: DatabaseError?) {

                            }

                            override fun onDataChange(p0: DataSnapshot?) {

                                Log.i("key",p0.toString())
                                if (p0!!.value != null) {
                                    getAllLocations((p0!!.value as Map<String, Any>?)!!)

                                }
                            }

                        })


/*
                        var latitudeKonum1:Double?=null
                        var longitudeKonum1:Double?=null
                        FirebaseDatabase.getInstance().reference
                                .child("turler1")
                                .orderByKey()
                                .addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onCancelled(p0: DatabaseError?) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot?) {

                                            var a = p0!!.childrenCount.toString()
                                             var td= p0!!.value as HashMap<String,Any>
                                            var c = td["latitude"]

                                            for (key in td.keys){

                                                var post= td[key] as HashMap<String,Any>

                                                //var b =
                                                Log.i("Keyb", td.toString())
                                                Log.i("Keyc",c.toString())



                                            }
                                            Log.i("Key",a)

                                            /*
                                            latitudeKonum1 = p0!!.getValue(Turler::class.java)!!.latitude
                                            longitudeKonum1 = p0!!.getValue(Turler::class.java)!!.longitude

                                            var konumGetir1 = LatLng(latitudeKonum1!!, longitudeKonum1!!)
                                            mMap!!.addMarker(MarkerOptions()
                                                    .position(konumGetir1)
                                                    .title("Yardım")
                                                    .snippet("Yardımın Bulunduğu Konum" + latitudeKonum1!! + "," + longitudeKonum1!!)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))
                                        */




                                    }




                                })


*/

/*
                        var latitudeKonum1:Double?=null
                        var longitudeKonum1:Double?=null
                                 FirebaseDatabase.getInstance().reference
                                         .child("turler1")
                                         .orderByKey()
                                .addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onCancelled(p0: DatabaseError?) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot?) {


                                        latitudeKonum1 = p0
                                        longitudeKonum1 = p0!!.getValue()
                                          val konumGetir1 = LatLng(latitudeKonum1!!, longitudeKonum1!!)
                                          mMap!!.addMarker(MarkerOptions()
                                                  .position(konumGetir1)
                                                  .title("Yardım")
                                                  .snippet("Yardımın Bulunduğu Konum" + latitudeKonum1!! + "," + longitudeKonum1!!)
                                                  .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))
                                      }




                                })

*/
/*
                        var a = ArrayList<Turler>()
                        var refarans=FirebaseDatabase.getInstance().reference
                                .child("turler")
                                .child(key)
                                .addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onCancelled(p0: DatabaseError?) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot?) {


                                        var b = p0!!.getValue(Turler::class.java)

                                        a.add(b!!)
                                        for (i in p0.children){
                                         val c = LatLng(b.latitude!!,b.longitude!!)
                                            mMap!!.addMarker(MarkerOptions()
                                                    .position(c)
                                                    .title("Yardım")
                                                    .snippet("Yardımın Bulunduğu Konum" + b.latitude!! + "," + b.longitude!!)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))
                                        }

                                    }

                                })

                                */



/*
                        var denemelatitude:Double?=null
                        var denemelongitude:Double?=null
                        var refarans=FirebaseDatabase.getInstance().reference
                                .child("turler")
                                .child(key)
                                .addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onCancelled(p0: DatabaseError?) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot?) {


                                            denemelatitude = p0!!.getValue(Turler::class.java)!!.latitude
                                            denemelongitude = p0!!.getValue(Turler::class.java)!!.longitude


                                            val konumGetir = LatLng(denemelatitude!!, denemelongitude!!)
                                            mMap!!.addMarker(MarkerOptions()
                                                    .position(konumGetir)
                                                    .title("Yardım")
                                                    .snippet("Yardımın Bulunduğu Konum" + denemelatitude!! + "," + denemelongitude!!)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))



                                    }

                                })
*/






                    }

                    Thread.sleep(1000)

                }catch (ex:Exception){}


            }

        }

    }


    var listkategori = ArrayList<Turler>()


    fun LoadKategori() {

        listkategori.add(Turler(R.drawable.food, "yiyecek", 39.932464, 32.846638))
        listkategori.add(Turler(R.drawable.home, "barınma", 39.933090, 32.843290))
        listkategori.add(Turler(R.drawable.home, "barınma", 39.931050, 32.844900))




        /*
        for(i in 0..listkategori.size-1){

            var newPockemon=listkategori[i]



            val pockemonLoc = LatLng(location!!.latitude, location!!.longitude)
            mMap!!.addMarker(MarkerOptions()
                    .position(pockemonLoc)
                    .title(newPockemon.kategori!!)
                    .snippet("Deneme")
                    .icon(BitmapDescriptorFactory.fromResource(newPockemon.turResim!!)))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(pockemonLoc,14f))

        }*/

    }



    override fun onMapClick (latLng : LatLng? ) {
        Toast.makeText(this@MapsActivity,
                "Konum:\n" + latLng?.latitude + " : " + latLng!!.longitude,
                Toast.LENGTH_LONG).show()
    }




    override fun onMapLongClick(latLng : LatLng?) {
        Toast.makeText(this@MapsActivity,
                "Konum:\n" + latLng?.latitude + " : " + latLng!!.longitude,
                Toast.LENGTH_LONG).show()
        //Add marker on LongClick position
        var markerOptions = MarkerOptions().position(latLng).title(latLng.toString())
        var oAnkiKonum = LatLng(latLng?.latitude, latLng!!.longitude)
        mMap!!.addMarker(MarkerOptions()
                .position(oAnkiKonum)
                .title("Yardım")
                .snippet("Yardımın Bulunduğu Konum" +oAnkiKonum.latitude!! +"," +oAnkiKonum.longitude!!)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel)))

        /*
        var ref=FirebaseDatabase.getInstance().reference
                .child("turler")
                .child("kategori")
                .child("odadaki_kullanicilar")
                .child(FirebaseAuth.getInstance().currentUser?.uid)
                .child("okunan_mesaj_sayisi")
                .setValue(toplamMesaj)
                */

        FirebaseDatabase.getInstance().getReference()
                .child("turler")
                .child("latitude")
                .setValue(oAnkiKonum.latitude!!)
        FirebaseDatabase.getInstance().getReference()
                .child("turler")
                .child("longitude")
                .setValue(oAnkiKonum.longitude!!)



        /*
        listkonum.add(Turler(R.drawable.ic_local_hotel, "yiyecek", 39.932464, 32.846638))
        FirebaseDatabase.getInstance().getReference()
                .child("turler1")
                .push()
                .child("latitude")
                .setValue(oAnkiKonum.latitude!!)
        FirebaseDatabase.getInstance().getReference()
                .child("turler1")
                .push()
                .child("longitude")
                .setValue(oAnkiKonum.longitude!!)

*/
/*


*/



        /*

        FirebaseDatabase.getInstance().getReference()
                .child("turler")
                .child(key)
                .child("latitude")
                .setValue(oAnkiKonum.latitude!!)
        FirebaseDatabase.getInstance().getReference()
                .child("turler")
                .child(key)
                .child("longitude")
                .setValue(oAnkiKonum.longitude!!)

*/


       // var locationData = LocationData(location!!.latitude, location!!.longitude)
        FirebaseDatabase.getInstance().getReference()
                .child("turler1")
                .child(Date().time.toString())
                .setValue(oAnkiKonum)

    }


     fun addGreenMarker(newDate: Date, latLng1: LatLng) {
        var dt = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        var markerOptions = MarkerOptions()
        markerOptions.position(latLng1)
        //markerOptions.title(dt.format(newDate))
        markerOptions.title("Yardım")
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hotel))
         markerOptions.snippet("Yardımın Bulunduğu Konum" +latLng1.latitude!! +"," +latLng1.longitude!!)
        mMap!!.addMarker(markerOptions)
    }

    fun getAllLocations(locations: Map<String, Any>) {


        for ((key, value) in locations) {

            var newDate = Date(java.lang.Long.valueOf(key)!!)
            //var singleLocation = value as Map<*, *>
            var singleLocation = value as Map<*, *>
            var latLng1 = LatLng(singleLocation["latitude"] as Double, singleLocation["longitude"] as Double)
            addGreenMarker(newDate, latLng1)
            Log.i("keylocation",singleLocation["latitude"].toString())

        }


    }



}
