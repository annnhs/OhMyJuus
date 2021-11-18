package com.lx.ohmyjuus

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.lx.ohmyjuus.databinding.ActivityMainBinding
import com.lx.ohmyjuus.jubging.JubgingActivity
import com.lx.ohmyjuus.weather.Adapter.ViewPagerAdapter
import com.lx.ohmyjuus.weather.Common.Common
import com.lx.ohmyjuus.weather.TodayWeatherFragment
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity() {

    private var viewPager: ViewPager? = null
    lateinit var binding: ActivityMainBinding
    private var drawerLayout: DrawerLayout? = null
    private var locationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    enum class LayoutItem {
        POINT, CHALLENGE, COMMUNITY, MAP, RECORD, PROFILE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.toolbar)

        binding.goJubgingBtn.setOnClickListener {
            val intent = Intent(this, JubgingActivity::class.java)
            startActivity(intent)
        }
        binding.moreChallengeButton.setOnClickListener {
            val intent = Intent(this, ChallengeActivity::class.java)
            startActivity(intent)
        }

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

        ////////////ㄱㄱㅎ////////////

        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout

        //Request permission

        //Request permission

        //Request permission
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        buildLocationRequest()
                        buildLocationCallBack()
                        fusedLocationProviderClient =
                            LocationServices.getFusedLocationProviderClient(this@MainActivity)
                        fusedLocationProviderClient?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.myLooper()
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    Snackbar.make(drawerLayout!!, "Permission Denied", Snackbar.LENGTH_LONG)
                        .show()
                }
            }).check()



        //줍깅 캠페인(이정환)
        binding.goSite1.setOnClickListener {
            val goSite = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.vision21.kr/mobile/article.html?no=157142")
            )
            startActivity(goSite)
        }
        binding.goSite2.setOnClickListener {
            val goSite2 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://familyseoul.or.kr/node/12669")
            )
            startActivity(goSite2)
        }
        binding.goSite3.setOnClickListener {
            val goSite3 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://sisatotalnews.com/article.php?aid=1636367633140948003")
            )
            startActivity(goSite3)
        }
        binding.goSite4.setOnClickListener {
            val goSite4 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.nwtnews.co.kr/news/articleView.html?idxno=85316")
            )
            startActivity(goSite4)
        }
        binding.goSite5.setOnClickListener {
            val goSite5 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.gwnews.org/news/articleView.html?idxno=223403")
            )
            startActivity(goSite5)
        }
        binding.goSite6.setOnClickListener {
            val goSite6 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.tynewspaper.co.kr/news/articleView.html?idxno=21320")
            )
            startActivity(goSite6)
        }
        binding.goSite7.setOnClickListener {
            val goSite7 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://jnnews.co.kr/news/view.php?idx=313942")
            )
            startActivity(goSite7)
        }



    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            @SuppressLint("WrongViewCast")
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Common.current_location = locationResult.lastLocation
                viewPager = findViewById<View>(R.id.view_pager) as ViewPager
                setupViewPager(viewPager!!)
                //                tabLayout = (TabLayout)findViewById(R.id.tabs);
//                tabLayout.setupWithViewPager(viewPager);

                // Log
                Log.d(
                    "Location",
                    locationResult.lastLocation.latitude.toString() + "/" + locationResult.lastLocation.longitude
                )
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayWeatherFragment.getInstance(), "Today")
        viewPager.adapter = adapter
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval(5000)
        locationRequest!!.setFastestInterval(3000)
        locationRequest!!.setSmallestDisplacement(10.0f)
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