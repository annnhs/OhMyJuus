package com.lx.ohmyjuus.jubging

//import com.example.jubging.BuildConfig
//import net.daum.mf.map.api.*

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.lx.ohmyjuus.R
import com.lx.ohmyjuus.databinding.ActivityJubgingBinding
import com.lx.ohmyjuus.jubging.MyService
import com.lx.ohmyjuus.jubging.MyUtils.Companion.activity
import java.io.FileOutputStream
import java.util.*
import kotlin.math.*


class JubgingActivity : AppCompatActivity()
 ,  SharedPreferences.OnSharedPreferenceChangeListener,
View.OnClickListener, LocationListener, OnMapReadyCallback,
GoogleMap.OnMarkerClickListener
{

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private var myReceiver: JubgingActivity.MyReceiver? = null

    private var mService: MyService? = null
    private var mBound = false
    private var isTracking = true

    //myservice 받아오기
    private var mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mBound = false
        }
    }


    private lateinit var binding: ActivityJubgingBinding


    private val TAG: String = JubgingActivity::class.java.getSimpleName()
    private var mMap: GoogleMap? = null
    private var mapView: MapView? = null

    private var locRequest: LocationRequest? = null
    private var fusedLocClient: FusedLocationProviderClient? = null
    private var locCallback: LocationCallback? = null
    private  var locCallback_walk:LocationCallback? = null
    private val latLngList = ArrayList<LatLng>()
    private var curLatlng: LatLng? = null
    private val SEOUL = LatLng(37.56, 126.97)
    private var marker: Marker? = null
    private var marker_juus: BitmapDescriptor? = null
    private var polyline: Polyline? = null


    protected fun createLocationRequest() {
        locRequest = LocationRequest()
        locRequest!!.setInterval(2000)
        locRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJubgingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.time.typeface = Typeface.DEFAULT_BOLD
        binding.distance.text = String.format("%.2f", MyUtils.totalDist)
        binding.distance.typeface = Typeface.DEFAULT_BOLD
        binding.btnStart.text = if (!MyUtils.startToggle) "줍깅 시작" else "줍깅 종료"
        MyUtils.activity = this
//
        if (!checkPermissions()) requestPermissions()


        myReceiver = MyReceiver()

        if (MyUtils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }


        binding.btnStart.setOnClickListener(this)
        binding.btnTracking.setOnClickListener(this)




        mapView = findViewById<View>(R.id.mapView) as MapView
        if (mapView != null) {
            mapView!!.onCreate(savedInstanceState)
            mapView!!.getMapAsync(this)
        }

        fusedLocClient = LocationServices.getFusedLocationProviderClient(this)
        setLocCallback()

        createLocationRequest()









    }

    fun updateTime(time: String) {
        binding.time.text = time
    }


    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            unbindService(mServiceConnection)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
//            Snackbar.make(
////                findViewById(R.id.activity_main),
//                R.string.permission_rationale,
//                Snackbar.LENGTH_INDEFINITE
//            )
//                .setAction(R.string.ok) { // Request permission
//                    ActivityCompat.requestPermissions(
//                        this@JubgingActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        REQUEST_PERMISSIONS_REQUEST_CODE
//                    )
//                }
//                .show()
        } else {
            Log.i(TAG, "Requesting permission")
            ActivityCompat.requestPermissions(
                this@JubgingActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty()) {
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService!!.requestLocationUpdates()
            } else {
//                Snackbar.make(
////                    findViewById(R.id.activity_main),
//                    R.string.permission_denied_explanation,
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                    .setAction(R.string.settings) {
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
//                    }
//                    .show()
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key.equals(MyUtils.KEY_REQUESTING_LOCATION_UPDATES)) {
        }
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val location = intent!!.getParcelableExtra<Location>(MyService.EXTRA_LOCATION)
            if (location != null) {

//                MyUtils.pushNewPoint(location)
                drawPolyline(latLngList)
                binding.distance.text = String.format("%.2f", MyUtils.totalDist)

            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start -> {
                if (!MyUtils.startToggle) {
                    if (!checkPermissions()) {
                        requestPermissions()
                    } else {
                        changeCallback(locCallback, locCallback_walk, true)
                        mService?.requestLocationUpdates()
                        binding.btnStart.text = "줍깅 종료"
                        MyUtils.startJubging()



                    }
                } else {
                    val dialog = myDialogFragment(object: myDialogFragment.OnClickDialogListener {
                        override fun onClickPositive(address: String) {

                            val jubgingInfo = address +"/"+binding.time.text+"/"+binding.distance.text


                            mService?.removeLocationUpdates()


                            val intent = Intent().apply {
                                putExtra("jubgingInfo", jubgingInfo)
                                putExtra("isJubgingOn", true)
                            }
                            activity!!.setResult(RESULT_OK, intent)
                            activity!!.finish()
                        }

                        override fun onClickNegative() {

                        }
                    })

                    changeCallback(locCallback_walk, locCallback, false)
//                    mOnCaptureClick(mapView)
                    CaptureMapScreen()
                    dialog.show(supportFragmentManager, "myDialog")
                }
            }


            R.id.btn_tracking -> {
                if (isTracking) {
                    isTracking = false
                    binding.btnTracking.background = getDrawable(R.drawable.ic_baseline_gps_not_fixed_24)
                } else {
                    isTracking = true
                    binding.btnTracking.background = getDrawable(R.drawable.ic_baseline_gps_fixed_24)

//                    if (MyUtils.pointList.isNotEmpty())
//                        mapView.setMapCenterPoint(MyUtils.pointList.last(), false)

                }
            }
        }
    }





    override fun onBackPressed() {
        intent.putExtra("isJubgingOn", MyUtils.startToggle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }




    // 구글맵 시작
    // 콜백 메소드 교체 (경로 그리기 <-> 마커 업데이트만)
    open fun changeCallback(
        callback1: LocationCallback?,
        callback2: LocationCallback?,
        isWalk: Boolean
    ) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (!isWalk) fusedLocClient!!.removeLocationUpdates(callback1)
        fusedLocClient!!.requestLocationUpdates(locRequest, callback2, null)
    }


    fun drawPolyline(pointList: List<LatLng?>?) {
        polyline?.remove()
        val polylineOptions = PolylineOptions()
            .color(Color.argb(255, 107, 190, 212))
            .width(20f)
            .addAll(pointList!!)
            .geodesic(true)
        polyline = mMap?.addPolyline(polylineOptions)
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
        startLocationUpdates()
                PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        bindService(
            Intent(this, MyService::class.java), mServiceConnection,
            BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
        startLocationUpdates()
                LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(MyService.ACTION_BROADCAST)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
        fusedLocClient!!.removeLocationUpdates(locCallback)
    }


    //location 콜백메소드 정의
    fun setLocCallback() {
        locCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) return
                for (location in locationResult.locations) {
                    setCurLatlng(location)
                }
            }
        }
        locCallback_walk = object : LocationCallback() {
            // 산책
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) return
                latLngList.add(curLatlng!!)
                drawPolyline(latLngList)
            }
        }
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocClient!!.requestLocationUpdates(locRequest, locCallback, null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //googleMap.setOnInfoWindowClickListener(this)
        setDefaultLoc(this)
        var bitmap_juus = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                "juus_logo", "drawable",
                packageName
            )
        )
        bitmap_juus = Bitmap.createScaledBitmap(bitmap_juus!!, 120, 120, false)
        marker_juus = BitmapDescriptorFactory.fromBitmap(bitmap_juus)
        setCurMarker()
    }

    //초기 위치 설정
    fun setDefaultLoc(context: Context) {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        var loc: Location? = null
        val result =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (result == PackageManager.PERMISSION_GRANTED) {
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        }
        curLatlng = if (loc != null) LatLng(loc.latitude, loc.longitude) else SEOUL
    }

    //위치 바뀔 때
    override fun onLocationChanged(location: Location) {
        curLatlng = LatLng(location.latitude, location.longitude)
        setCurMarker()
    }

    // set current latlng
    fun setCurLatlng(location: Location) {
        curLatlng = LatLng(location.latitude, location.longitude)
        setCurMarker()
    }

    // update current marker position
    fun setCurMarker() {
        if (marker != null) marker!!.setPosition(curLatlng!!) else {
            val markerOptions = MarkerOptions()
            markerOptions.position(curLatlng!!)
                .icon(marker_juus)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng!!, 18f))
            marker = mMap!!.addMarker(markerOptions)
            marker!!.showInfoWindow()
        }
    }

    fun onStatusChanged(s: String?, i: Int, bundle: Bundle?) {}

    fun onProviderEnabled(s: String?) {}

    fun onProviderDisabled(s: String?) {}




    override fun onMarkerClick(marker: Marker): Boolean {
        val center = CameraUpdateFactory.newLatLng(marker.position)
        mMap!!.animateCamera(center)
        return false
    }


//    //캡쳐버튼클릭
//    fun mOnCaptureClick(v: View?) {
//        //전체화면
//        val rootView = findViewById<View>(R.id.mapView) as MapView
//        val screenShot = ScreenShot(rootView)
//        if (screenShot != null) {
//            //갤러리에 추가
//            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)))
//        }
//    }
//
//    //화면 캡쳐하기
//    fun ScreenShot(view: View): File? {
//        view.isDrawingCacheEnabled = true //화면에 뿌릴때 캐시를 사용하게 한다
//        val screenBitmap = view.drawingCache //캐시를 비트맵으로 변환
//        val filename = "screenshot.png"
//        val file = File(
//            Environment.getExternalStorageDirectory().toString() + "/Pictures",
//            filename
//        ) //Pictures폴더 screenshot.png 파일
//        var os: FileOutputStream? = null
//        try {
//            os = FileOutputStream(file)
//            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os) //비트맵을 PNG파일로 변환
//            os.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            return null
//        }
//        view.isDrawingCacheEnabled = false
//        return file
//    }


    fun CaptureMapScreen() {
        val callback: SnapshotReadyCallback = object : SnapshotReadyCallback {
            var bitmap: Bitmap? = null
            override fun onSnapshotReady(snapshot: Bitmap?) {
                // TODO Auto-generated method stub
                bitmap = snapshot
                try {
                    val out = FileOutputStream(
                        "/mnt/sdcard/"
                                + "MyMapScreen" + System.currentTimeMillis()
                                + ".png"
                    )

                    // above "/mnt ..... png" => is a storage path (where image will be stored) + name of image you can customize as per your Requirement
                    bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, out)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        mMap?.snapshot(callback)

        // myMap is object of GoogleMap +> GoogleMap myMap;
        // which is initialized in onCreate() =>
        // myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_pass_home_call)).getMap();
    }

    
}