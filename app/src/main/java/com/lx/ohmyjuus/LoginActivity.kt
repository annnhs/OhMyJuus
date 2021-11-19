package com.lx.ohmyjuus

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lx.ohmyjuus.api.JUUSClient
import com.lx.ohmyjuus.response.LoginRes
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val TAG: String = "LoginActivity"


//    쉐어드로부터 저장된 id, pw 가져오기
//    val sharedPrefs : SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                println("userLogin onResponse called : ${response.body()?.output.toString()}")

                var userData = response.body()?.output
//                userData.apply {
//                    //여기서 디비의 아이디 비번이랑 비교해야 되는데
//                }
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

            // 유저가 입력한 id, pw를 쉐어드에 저장한다.
            SharedPreferences.setUserId(this, userId)
            SharedPreferences.setUserPass(this, userPw)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}