package com.lx.ohmyjuus

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
//        val sharedPrefs : SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)

        val savedId = com.lx.ohmyjuus.SharedPreferences.getUserId(this)
//        val savedNick = sharedPrefs.getString("userNick", "")
//        val savedName = sharedPrefs.getString("userName", "")
//        val savedMobile = sharedPrefs.getString("userMobile", "")
//        binding.navigationView.getHeaderView(0).navId.text = savedNick
//        binding.navigationView.getHeaderView(0).navName.text = savedName

        // logout
        binding.navigationView.getHeaderView(0).navLogout.setOnClickListener {
            com.lx.ohmyjuus.SharedPreferences.clearUser(this)

            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        /////////////////////// login end /////////////////////////


        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navMap -> {
                    val intent = Intent(this, MapActivity::class.java)
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
                    Toast.makeText(this, "모든 권한이 부여됨", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "권한 중에 거부된 권한들: $deniedList", Toast.LENGTH_LONG).show()
                }
            }


    }



    fun onLayoutSelected(item: MainActivity.LayoutItem, bundle: Bundle?=null) {
        when(item) {
            MainActivity.LayoutItem.MAP -> {
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