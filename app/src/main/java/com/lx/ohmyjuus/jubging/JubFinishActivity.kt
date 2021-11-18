package com.lx.ohmyjuus.jubging

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lx.ohmyjuus.databinding.ActivityJubfinishBinding
import android.graphics.Bitmap

import android.content.Intent
import android.os.Parcelable


class JubFinishActivity: AppCompatActivity() {


    private lateinit var binding: ActivityJubfinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJubfinishBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //MyUtils.capture()
        binding.timeTextView.typeface = Typeface.DEFAULT_BOLD
        binding.timeTextView.text = MyUtils.time
        binding.distanceTextView.typeface = Typeface.DEFAULT_BOLD
        binding.distanceTextView.text = String.format("%.2f", MyUtils.totalDist)


        binding.jubCount.text = intent.getStringExtra("jubCount")



        val intent = intent

        val snapshot = intent.getParcelableExtra<Parcelable>("snapshot") as Bitmap?

//        binding.captureImageView.setImageResource(snapshot)
    }





}