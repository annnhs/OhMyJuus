package com.lx.ohmyjuus

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.ohmyjuus.databinding.ActivityMapBinding

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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17.0f))
        showMarker(curPoint)
    }

    fun showMarker(curPoint: LatLng) {
        mymarkerObj?.remove()
        mymarker = MarkerOptions()
        mymarker?.apply {
            title("내 위치")
            position(curPoint)
//            icon(BitmapDescriptorFactory.fromResource(com.google.android.gms.location.R.drawable.location))
//            mymarkerObj = map.addMarker(mymarker)
        }
    }
}