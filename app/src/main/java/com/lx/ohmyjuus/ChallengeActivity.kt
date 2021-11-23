package com.lx.ohmyjuus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.lx.ohmyjuus.databinding.ActivityChallengeBinding
import com.lx.ohmyjuus.point.PointActivity

class ChallengeActivity : AppCompatActivity() {

    lateinit var binding: ActivityChallengeBinding
    private var drawerLayout: DrawerLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
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

        binding.progressBar01.progress=90
        binding.progressBar02.progress=80
        binding.progressBar03.progress=60
        binding.progressBar04.progress=60
        binding.progressBar05.progress=30
        binding.progressBar06.progress=20
        binding.progressBar07.progress=10


        binding.progressBar08.progress=0


    }



}