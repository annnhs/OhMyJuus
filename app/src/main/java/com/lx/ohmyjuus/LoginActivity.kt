package com.lx.ohmyjuus

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lx.ohmyjuus.api.JUUSClient
import com.lx.ohmyjuus.databinding.ActivityLoginBinding
import com.lx.ohmyjuus.response.LoginRes
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val TAG: String = "LoginActivity"
    lateinit var binding: ActivityLoginBinding

//    쉐어드로부터 저장된 id, pw 가져오기
//    val sharedPrefs : SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(SharedPreferences.getUserId(this).isNullOrBlank() || SharedPreferences.getUserPass(this).isNullOrBlank()) {
            logInButton.setOnClickListener {
                login()
            }
        } else {
            Toast.makeText(this, "${SharedPreferences.getUserId(this)}님 " +
                    "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //회원가입 버튼
        goSignUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }

    fun userLogin(id:String, pw:String) {
        JUUSClient.api.userLogin(
            userId = id,
            userPw = pw
        ).enqueue(object: Callback<LoginRes> {
            override fun onResponse(
                call: Call<LoginRes>,
                response: Response<LoginRes>
            ) {
                val userData : LoginRes? = response.body()
                println("userLogin onResponse called : ${userData.toString()}")

                if(userData!=null) {
                    if(userData.output.isNotEmpty()) {
                        // 값 저장
//                        val sharedPreferences = getSharedPreferences("mine", 0)
//                        val editor = sharedPreferences.edit()
//                        editor.putString("id",  binding.loginId.text.toString())
//                        editor.putInt("loginCheck",  1)
//                        editor.putString("nickname", userData.output.get(0).nickname)
//                        editor.putString("sex", userData.output.get(0).sex)
//                        editor.apply()
//                        Log.d("LoginActivity loginCheck", sharedPreferences?.getInt("loginCheck", 0).toString())
//
//                        Toast.makeText(applicationContext, "${userData.output.get(0).nickname}님 환영합니다", Toast.LENGTH_SHORT).show()
//                        startActivity(intent)

                    }else {
                        Toast.makeText(applicationContext, "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()

                    }
                }

            }
            override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                println("login onFailure 호출됨")
            }
        })
    }

    fun login() {
        if(loginId.text.isNullOrBlank() || loginPassword.text.isNullOrBlank()) {
            Toast.makeText(this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
        } else {
            //editText로부터 입력된 값을 받아온다
            var userId = loginId.text.toString()
            var userPw = loginPassword.text.toString()

            //쿼리문 실행해보고
            userLogin(userId, userPw)

            val prefs : android.content.SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)
            val editor = prefs?.edit()
            editor?.putString("userId", userId)
            editor?.putString("userPw", userPw)
            editor?.commit()

            // 유저가 입력한 id, pw를 쉐어드에 저장한다.
            SharedPreferences.setUserId(this, userId)
            SharedPreferences.setUserPass(this, userPw)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}