package com.lx.ohmyjuus

import android.Manifest
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.lx.ohmyjuus.api.JUUSClient
import com.lx.ohmyjuus.databinding.ActivityMapBinding
import com.lx.ohmyjuus.point.PointActivity
import com.lx.ohmyjuus.response.MapRes
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding

    lateinit var map: GoogleMap


    var mymarker: MarkerOptions? = null
    var mymarkerObj: Marker? = null


    //위치 클라이언트 선언 -> fun requestLocation()
    var locationClient: FusedLocationProviderClient? = null

    var lat: Double? = null
    var log: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it

            requestLocation()

        }

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navMain -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                R.id.navMap -> {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)

                }
                R.id.navRecord -> {
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)

                }
                R.id.navPoint -> {
                    val intent = Intent(this, PointActivity::class.java)
                    startActivity(intent)

                }
                R.id.navChallenge -> {
                    val intent = Intent(this, ChallengeActivity::class.java)
                    startActivity(intent)
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }


        // 위험 권한 부여하기
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    //Toast.makeText(this, "모든 권한이 부여됨", Toast.LENGTH_LONG).show()
                } else {
                    //Toast.makeText(this, "권한 중에 거부된 권한들: $deniedList", Toast.LENGTH_LONG).show()
                }
            }


    }

    fun requestLocation() {
        //위치 클라이언트 가져오기
        locationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            //최근 위치 확인하기
            locationClient?.lastLocation?.addOnSuccessListener {
                println("최근 위치 : ${it?.latitude}, ${it?.longitude}")
            }?.addOnFailureListener {
                println("최근 위치 확인 시 에러 : ${it.message}")
            }

            //현재 위치 확인
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000 * 1000 //1000초마다 내 위치로 포커스
            }

            val locationCallback = object: LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    result.run {
                        for((index, location) in locations.withIndex()) {
                            println("현재 위치 : ${location.latitude}, ${location.longitude}")

                            showCurrentLocation(location)
                        }
                    }
                }
            }

            locationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        } catch (e:SecurityException) {
            e.printStackTrace()
        }
    }

    fun showCurrentLocation(location: Location) {
        val curPoint = LatLng(location.latitude, location.longitude)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18.0f))

        showMarker(curPoint)

        showTrashPinPoint("일반쓰레기통")
        showRecyclePinPoint("재활용쓰레기통")
        showUpcyclePinPoint("업사이클링매장")
    }

    fun showMarker(curPoint: LatLng) {
        mymarkerObj?.remove()
        mymarker = MarkerOptions()


        mymarker?.apply {
            title("내 위치")
            position(curPoint)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
            mymarkerObj = map.addMarker(mymarker!!)
        }
    }

    fun showTrashPinPoint(type: String) {
//        val point = "POINT(${location.longitude} ${location.latitude})"

        // 웹서버로 리스트 요청
        JUUSClient.api.getTrashPoint(
            type = type

        ).enqueue(object: Callback<MapRes> {
            override fun onResponse(call: Call<MapRes>, response: Response<MapRes>) {
                println("showTrashPoint onResponse 호출됨")


                var items = response.body()?.data
                items?.apply {
                    for (item in this) {
                        val trashPointMarker = MarkerOptions()
                        with(trashPointMarker) {
                            position(LatLng(item.latitude!!, item.longitude!!))
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_recyclepin))
                            map.addMarker(this)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MapRes>, t: Throwable) {
                println("showTrashPoint onFailure 호출됨")
            }

        })
    }

    fun showRecyclePinPoint(type: String) {
//        val point = "POINT(${location.longitude} ${location.latitude})"

        // 웹서버로 리스트 요청
        JUUSClient.api.getTrashPoint(
            type = type

        ).enqueue(object: Callback<MapRes> {
            override fun onResponse(call: Call<MapRes>, response: Response<MapRes>) {
                println("showRecyclePinPoint onResponse 호출됨")


                var items = response.body()?.data
                items?.apply {
                    for (item in this) {
                        val trashPointMarker = MarkerOptions()
                        with(trashPointMarker) {
                            position(LatLng(item.latitude!!, item.longitude!!))
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trashpin))
                            map.addMarker(this)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MapRes>, t: Throwable) {
                println("showRecyclePinPoint onFailure 호출됨")
            }

        })
    }

    fun showUpcyclePinPoint(type: String) {
//        val point = "POINT(${location.longitude} ${location.latitude})"

        // 웹서버로 리스트 요청
        JUUSClient.api.getTrashPoint(
            type = type

        ).enqueue(object: Callback<MapRes> {
            override fun onResponse(call: Call<MapRes>, response: Response<MapRes>) {
                println("showUpcyclePinPoint onResponse 호출됨")


                var items = response.body()?.data
                items?.apply {
                    for (item in this) {
                        val trashPointMarker = MarkerOptions()
                        with(trashPointMarker) {
                            position(LatLng(item.latitude!!, item.longitude!!))
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shoppin))
                            map.addMarker(this)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MapRes>, t: Throwable) {
                println("showUpcyclePinPoint onFailure 호출됨")
            }

        })
    }


}