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
import com.lx.ohmyjuus.response.LoginRes
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.inputId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val TAG: String = "LoginActivity"

    // 쉐어드로부터 저장된 id, pw 가져오기
    val sharedPreference = getSharedPreferences("USER", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if(sharedPreference.getString("userId", "").isNullOrBlank() || sharedPreference.getString("userPw", "").isNullOrBlank()) {
            logInButton.setOnClickListener {
                login()
            }
        } else {
            Toast.makeText(this, "${sharedPreference.getString("userId", "")}님 " +
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
                println("login onResponse 호출됨")

                var userData = response.body()?.output
                userData.apply {
                    //여기서 디비의 아이디 비번이랑 비교해야 되는데
                }
            }
            override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                println("login onFailure 호출됨")
            }
        })
    }

    fun login() {
        if(inputId.text.isNullOrBlank() || inputPw.text.isNullOrBlank()) {
            Toast.makeText(this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
        } else {
            //editText로부터 입력된 값을 받아온다
            var userId = inputId.text.toString()
            var userPw = inputPw.text.toString()

            //쿼리문 실행해보고
            userLogin(userId, userPw)

            // 유저가 입력한 id, pw를 쉐어드에 저장한다.
            val editor = sharedPreference.edit()
            editor.putString("userId", userId)
            editor.putString("userPw", userPw)
            editor.apply()

//            val savedId = sharedPreference.getString("userId", "dong")
//            val savedPw = sharedPreference.getString("userPw", "dong")
//
//            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
//            if(userId == savedId && userPw == savedPw){
////                getUserLogin(id, pw)
//
            // 로그인 성공 다이얼로그 보여주기
//                dialog("success")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
//            }
//            else{
//                // 로그인 실패 다이얼로그 보여주기
//                dialog("fail")
//            }
        }
    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)
        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }
        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }
        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}