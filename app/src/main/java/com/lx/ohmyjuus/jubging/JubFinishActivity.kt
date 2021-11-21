package com.lx.ohmyjuus.jubging

import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lx.ohmyjuus.databinding.ActivityJubfinishBinding
import android.graphics.Bitmap

import android.content.Intent
import android.location.Location
import android.os.Parcelable
import com.lx.ohmyjuus.jubging.MyUtils
import com.lx.ohmyjuus.MainActivity


class JubFinishActivity: AppCompatActivity() {


    private lateinit var binding: ActivityJubfinishBinding
//    private var myReceiver: JubFinishActivity.MyReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJubfinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        myReceiver = JubgingActivity.MyReceiver()


        //MyUtils.capture()
        binding.finishTime.typeface = Typeface.DEFAULT_BOLD
        binding.finishTime.text = MyUtils.time
        binding.finishiDistance.typeface = Typeface.DEFAULT_BOLD
        //binding.finishiDistance.text = String.format("%.2f", MyUtils.totalDist)

//        binding.finishiDistance.text = intent.extras!!.getString("distance")

//        binding.finishCount.text = intent.getStringExtra("jubCount")


        binding.goHomeButton.setOnClickListener {



            val goHome= Intent(this, MainActivity::class.java)
            startActivity(goHome)

        }

        val intent = intent

        val snapshot = intent.getParcelableExtra<Parcelable>("snapshot") as Bitmap?

//        binding.captureImageView.setImageResource(snapshot)
    }

//    inner class MyReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val location = intent!!.getParcelableExtra<Location>(MyService.EXTRA_LOCATION)
//            if (location != null) {
//
//                MyUtils.pushNewPoint(location)
//                binding.finishiDistance.text = String.format("%.2f", MyUtils.totalDist)
//
//            }
//        }
//    }



}