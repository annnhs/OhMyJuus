package com.lx.ohmyjuus

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lx.ohmyjuus.api.JUUSClient
import com.lx.ohmyjuus.response.RegisterRes
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    val TAG: String = "SignupActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUpButton.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val userId = inputId.text.toString()
            val userPw = inputPw.text.toString()
            val userName = inputName.text.toString()
            val userMobile = inputMobile.text.toString()
            val userNick = inputNick.text.toString()
            val userBirth = inputBirth.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(userId.isEmpty() || userPw.isEmpty() || userName.isEmpty() || userBirth.isEmpty() || userMobile.isEmpty() || userNick.isEmpty()){
                Toast.makeText(this, "입력란을 모두 작성해주세요!", Toast.LENGTH_SHORT).show()
            }else {
                userRegister(userId, userPw, userName, userMobile, userNick, userBirth)

                // 회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

//                val sharedPrefs : SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)
//                val editor = sharedPrefs.edit()
                val prefs : SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)
                val editor = prefs?.edit()
                editor?.putString("userId", userId)
                editor?.putString("userPw", userPw)
                editor?.putString("userName", userName)
                editor?.putString("userMobile", userMobile)
                editor?.putString("userNick", userNick)
                editor?.putString("userBirth", userBirth)
                editor?.commit()

                // 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }

    fun userRegister(id:String, pw:String, name:String, mobile:String, nickName:String, birth:String) {
        JUUSClient.api.userRegister(
            userId = id,
            userPw = pw,
            userName = name,
            userMobile = mobile,
            userNick = nickName,
            userBirth = birth
        ).enqueue(object: Callback<RegisterRes> {
            override fun onResponse(
                call: Call<RegisterRes>,
                response: Response<RegisterRes>
            ) {
                if (response.body()?.output?.affectedRows!! > 0) {
                    println("회원 가입 정보 추가 성공")
                }
            }
            override fun onFailure(call: Call<RegisterRes>, t: Throwable) {
                println(" userRegister onFailure 호출됨")
            }
        })
    }

}