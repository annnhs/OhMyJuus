package com.lx.ohmyjuus.point

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.ohmyjuus.R
import com.lx.ohmyjuus.databinding.ActivityShopPlasticDetailBinding

class ShopGlassDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityShopPlasticDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopPlasticDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}