package com.lx.ohmyjuus.point

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.ohmyjuus.R
import com.lx.ohmyjuus.databinding.ActivityShopdetailBinding

class ShopdetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityShopdetailBinding
    lateinit var map: GoogleMap

    var shopmarker: MarkerOptions? = null
    var shopmarkerObj: Marker? = null

    //위치 클라이언트 선언 -> fun requestLocation()
    var locationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mapFragment = supportFragmentManager.findFragmentById(R.id.shopdetailmap) as SupportMapFragment
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

                            shopdetailPoint(location)
                        }
                    }
                }
            }

            locationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        } catch (e:SecurityException) {
            e.printStackTrace()
        }
    }


    fun shopdetailPoint(location: Location) {
        val shopdetailPoint = LatLng(37.50799528351815, 127.040862488328)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(shopdetailPoint, 18.0f))

        showMarker(shopdetailPoint)

    }

    fun showMarker(shopdetailPoint: LatLng) {
        shopmarkerObj?.remove()
        shopmarker = MarkerOptions()


        shopmarker?.apply {
            title("내 위치")
            position(shopdetailPoint)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shoppin))
            shopmarkerObj = map.addMarker(shopmarker!!)
        }
    }


}