package com.lx.ohmyjuus.point

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.lx.ohmyjuus.databinding.ActivityPointBinding
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.lx.ohmyjuus.*

class PointActivity : AppCompatActivity() {

    lateinit var binding: ActivityPointBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpTabBar()

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

    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
//                R.id.challengeIng -> binding.textMain.text = "진행중인 챌린지"
//                R.id.challengeEd -> binding.textMain.text = "Chat"
                R.id.pointView -> {
                    var pointviewFrag = PointViewFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.bottomPoint, pointviewFrag).commit()

                }
                R.id.pointUse -> {
//                    binding.textMain.text = "내 챌린지"
                    var pointuseFrag = PointUseFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.bottomPoint, pointuseFrag)
                        .commit()
                }
            }
        }
    }

}