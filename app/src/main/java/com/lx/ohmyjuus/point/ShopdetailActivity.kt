package com.lx.ohmyjuus.point

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.ohmyjuus.R
import com.lx.ohmyjuus.databinding.ActivityShopdetailBinding

class ShopdetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityShopdetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}