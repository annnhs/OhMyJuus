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
    var isExistBlank = false
    var isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signUpButton.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val userId = ID.text.toString()
            val userPw = inputPW.text.toString()
            val userName = inputName.text.toString()
            val userMobile = inputMobile.text.toString()
            val userNick = inputNick.text.toString()
            val userBirth = inputBirth.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(userId.isEmpty() || userPw.isEmpty() || userName.isEmpty() || userBirth.isEmpty() || userMobile.isEmpty() || userNick.isEmpty()){
                isExistBlank = true
            }else {
                isPWSame = true
            }

            if(!isExistBlank && isPWSame) {
                userRegister(userId, userPw, userName, userMobile, userNick, userBirth)

                // 회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                val sharedPrefs : SharedPreferences = applicationContext.getSharedPreferences("USER", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.putString("userName", userName)
                editor.putString("userMobile", userMobile)
//                editor.putString("userNick", userNick)
                editor.putString("userBirth", userBirth)
                editor.apply()

                // 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else {
                // 상태에 따라 다른 다이얼로그 띄워주기
                if(isExistBlank){   // 작성 안한 칸이 있을 경우
                    dialog("blank")
                }
                else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                }
            }
        }
    }

    fun userRegister(id:String, pw:String, name:String, mobile:String, nick:String, birth:String) {
        JUUSClient.api.userRegister(
            userId = id,
            userPw = pw,
            userName = name,
            userMobile = mobile,
            userNick = nick,
            userBirth = birth
        ).enqueue(object: Callback<RegisterRes> {
            override fun onResponse(
                call: Call<RegisterRes>,
                response: Response<RegisterRes>
            ) {
                if (response.body()?.output?.affectedRows!! > 0) {
                    println("회원 정보 추가 성공")
                }
            }
            override fun onFailure(call: Call<RegisterRes>, t: Throwable) {
                println("onFailure 호출됨")
            }
        })
    }

    fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)
        // 빈 칸 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 비밀번호 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }
        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }
        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}