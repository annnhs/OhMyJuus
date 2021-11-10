package com.lx.ohmyjuus

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UpdateMypageActivity : AppCompatActivity() {

    // 쉐어드로부터 저장된 회원정보 가져오기
    val sharedPreference = getSharedPreferences("USER", Context.MODE_PRIVATE)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_mypage)


    }


}