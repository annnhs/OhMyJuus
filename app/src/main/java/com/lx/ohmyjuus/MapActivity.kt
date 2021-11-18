package com.lx.ohmyjuus

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.lx.map.api.SmokeAreaClient
import com.lx.ohmyjuus.api.JUUSClient
import com.lx.ohmyjuus.databinding.ActivityMapBinding
import com.lx.ohmyjuus.response.MapRes
import com.lx.ohmyjuus.response.SmokeAreaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding

    lateinit var map: GoogleMap


    var mymarker: MarkerOptions? = null
    var mymarkerObj: Marker? = null
    private var marker_juus: BitmapDescriptor? = null


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

            showTrashPoint("일반쓰레기통")


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

        getAreaList(location)



    }

    fun showMarker(curPoint: LatLng) {
        mymarkerObj?.remove()
        mymarker = MarkerOptions()

        var bitmap_juus = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                "juus_logo", "drawable",
                packageName
            )
        )
        bitmap_juus = Bitmap.createScaledBitmap(bitmap_juus!!, 120, 120, false)
        marker_juus = BitmapDescriptorFactory.fromBitmap(bitmap_juus)

        mymarker?.apply {
            title("내 위치")
            position(curPoint)
            icon(marker_juus)
            mymarkerObj = map.addMarker(mymarker!!)
        }
    }

    fun showTrashPoint(type: String) {
//        val point = "POINT(${location.longitude} ${location.latitude})"

        // 웹서버로 리스트 요청
        JUUSClient.api.getTrashPoint(
            type = type

        ).enqueue(object: Callback<MapRes> {
            override fun onResponse(call: Call<MapRes>, response: Response<MapRes>) {
                println("onResponse 호출됨")

                var items = response.body()?.data
                items?.apply {
                    for (item in this) {
                        val trashPointMarker = MarkerOptions()
                        with(trashPointMarker) {
                            position(LatLng(item.latitude!!, item.longitude!!))
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trashcan))
                            map.addMarker(this)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MapRes>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })
    }

    fun getAreaList(location: Location) {
        val point = "POINT(${location.longitude} ${location.latitude})"
        // 웹서버로 리스트 요청
        SmokeAreaClient.api.getSmokeArea(
            point1=point,
            point2=point
        ).enqueue(object: Callback<SmokeAreaResponse> {
            override fun onResponse(call: Call<SmokeAreaResponse>, response: Response<SmokeAreaResponse>) {
                println("onResponse 호출됨")

                var items = response.body()?.data
                items?.apply {
//                    smokingAdapter?.items = this
//                    smokingAdapter?.notifyDataSetChanged()
                }

                items?.apply {
                    for (item in this) {
                        val smokeAreaMarker = MarkerOptions()
                        with(smokeAreaMarker) {
                            position(LatLng(item.latitude!!, item.longitude!!))
                            title(item.smokingName)
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.juus_logo))
                            map.addMarker(this)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SmokeAreaResponse>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })
    }


}