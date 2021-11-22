package com.lx.ohmyjuus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.lx.ohmyjuus.databinding.ActivityMypageBinding
import com.lx.ohmyjuus.point.PointActivity

class MyPageActivity: AppCompatActivity()  {

    lateinit var binding: ActivityMypageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)



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




    }


}