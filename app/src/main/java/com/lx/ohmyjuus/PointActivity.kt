package com.lx.ohmyjuus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.ohmyjuus.databinding.ActivityPointBinding
import androidx.appcompat.app.AppCompatDelegate

class PointActivity : AppCompatActivity() {

    lateinit var binding: ActivityPointBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpTabBar()



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