package com.lx.ohmyjuus

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.ohmyjuus.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity() {
    //공경화
    //정은빈
    lateinit var binding: ActivityMainBinding

    enum class LayoutItem {
        POINT, CHALLENGE, COMMUNITY, MAP, RECORD, PROFILE
    }

    lateinit var map:GoogleMap

    var mymarker: MarkerOptions? = null
    var mymarkerObj: Marker? = null

    var lat: Double? = null
    var log: Double? = null


    //위치 클라이언트 선언 -> fun requestLocation()
    var locationClient:FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        /////////////////////// login /////////////////////////

        // 쉐어드로부터 저장된 id, pw 가져오기
        val sharedPreference = getSharedPreferences("USER", Context.MODE_PRIVATE)

        val savedId = sharedPreference.getString("userId", "")
        val savedNick = sharedPreference.getString("userNick", "")
        val savedName = sharedPreference.getString("userName", "")
        val savedMobile = sharedPreference.getString("userMobile", "")
        binding.navigationView.getHeaderView(0).navId.text = savedNick
        binding.navigationView.getHeaderView(0).navName.text = savedName

        if(savedId.isNullOrBlank() || savedNick.isNullOrBlank()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        // logout
        binding.navigationView.getHeaderView(0).navLogout.setOnClickListener {
            val editor = sharedPreference.edit()
            editor.clear()
            editor.commit()

            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        /////////////////////// login /////////////////////////


        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navMap -> {
                    onLayoutSelected(LayoutItem.MAP, null)
                    //
                    var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync {
                        map = it

                        requestLocation()
                    }
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
                    Toast.makeText(this, "모든 권한이 부여됨", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "권한 중에 거부된 권한들: $deniedList", Toast.LENGTH_LONG).show()
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
            mymarkerObj = map.addMarker(mymarker)
        }
    }

    fun onLayoutSelected(item:LayoutItem, bundle: Bundle?=null) {
        when(item) {
            LayoutItem.MAP -> {
                binding.toolbar.title = ""

//                binding.layoutFirst.visibility = View.VISIBLE
//                binding.layoutSecond.visibility = View.GONE
//                binding.layoutThird.visibility = View.GONE
//                binding.layoutFourth.visibility = View.GONE
//                binding.layoutService.visibility = View.GONE
            }
//            LayoutItem.SECOND -> {
//                binding.toolbar.title = ""
//
//                binding.layoutFirst.visibility = View.GONE
//                binding.layoutSecond.visibility = View.VISIBLE
//                binding.layoutThird.visibility = View.GONE
//                binding.layoutFourth.visibility = View.GONE
//                binding.layoutService.visibility = View.GONE
//            }
//            LayoutItem.THIRD -> {
//                binding.toolbar.title = ""
//
//                binding.layoutFirst.visibility = View.GONE
//                binding.layoutSecond.visibility = View.GONE
//                binding.layoutThird.visibility = View.VISIBLE
//                binding.layoutFourth.visibility = View.GONE
//                binding.layoutService.visibility = View.GONE
//            }
//            LayoutItem.FOURTH -> {
//                binding.toolbar.title = ""
//
//                binding.layoutFirst.visibility = View.GONE
//                binding.layoutSecond.visibility = View.GONE
//                binding.layoutThird.visibility = View.GONE
//                binding.layoutFourth.visibility = View.VISIBLE
//                binding.layoutService.visibility = View.GONE
//
//            }
//            LayoutItem.SERVICE -> {
//                binding.toolbar.title = ""
//
//                binding.layoutFirst.visibility = View.GONE
//                binding.layoutSecond.visibility = View.GONE
//                binding.layoutThird.visibility = View.GONE
//                binding.layoutFourth.visibility = View.GONE
//                binding.layoutService.visibility = View.VISIBLE
//            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}