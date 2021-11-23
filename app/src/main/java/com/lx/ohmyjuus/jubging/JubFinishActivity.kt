package com.lx.ohmyjuus.jubging

import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lx.ohmyjuus.databinding.ActivityJubfinishBinding
import android.graphics.Bitmap

import android.content.Intent
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.ImageView
import com.lx.ohmyjuus.jubging.MyUtils
import com.lx.ohmyjuus.MainActivity
import com.lx.ohmyjuus.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import java.lang.Exception


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




//        binding.finishiDistance.text = intent.extras!!.getString("distance")

//        binding.finishCount.text = intent.getStringExtra("jubCount")

//
//        binding.finalImageView


        binding.goHomeButton.setOnClickListener {

            val goHome= Intent(applicationContext, MainActivity::class.java)
            startActivity(goHome)

        }

        val intent = intent


        val extras = getIntent().extras
        val Path = extras?.getString("filePath")

        try {
            val files = File(Path)
            if (files.exists() == true) {
                val myBitmap = BitmapFactory.decodeFile(files.absolutePath)
                binding.finalImageView.setImageBitmap(myBitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val finishDist = extras?.getString("distance")
        println("finishDist: $finishDist")
        binding.finishiDistance.text = finishDist

    }


    fun takeSnapShot() {
        val snapshotIntent = intent

        val takeSnapshot = snapshotIntent.extras!!.getInt("capture") //intent.getIntExtra("age") 라고해도됨


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
