package com.lx.ohmyjuus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lx.ohmyjuus.databinding.ActivityUpdateMypageBinding
import kotlinx.android.synthetic.main.activity_update_mypage.*

class UpdateMypageActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateMypageBinding

    // 쉐어드로부터 저장된 회원정보 가져오기
    val sharedPreference = getSharedPreferences("USER", Context.MODE_PRIVATE)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedId = sharedPreference.getString("userId", "")
//        val savedPw = sharedPreference.getString("userPw", "")
        val savedNick = sharedPreference.getString("userNick", "")
//        val savedName = sharedPreference.getString("userName", "")
        val savedMobile = sharedPreference.getString("userMobile", "")
//        val savedBirth = sharedPreference.getString("userBirth", "")
//        val savedHeight = sharedPreference.getInt("userHeight", 0)
//        val savedWeight = sharedPreference.getInt("userWeight", 0)

        binding.editId.text = savedId
        binding.editMobile.hint = "현재 전화번호 : " + savedMobile
        binding.editNick.hint = "현재 닉네임 : " + savedNick


        binding.imageButton.setOnClickListener {
            val newPw = binding.editPw.text.toString()
            val newMobile = binding.editMobile.text.toString()
            val newNick = binding.editNick.text.toString()
            val height = binding.editHeight.text.toString()
            val weight = binding.editWeight.text.toString()
            if(newPw.isEmpty() || newMobile.isEmpty() || newNick.isEmpty() || height.isEmpty() || weight.isEmpty()) {
                showToast("빈 칸 없이 입력해주세요.")
            }else {
                showToast("수정되었습니다!")
                val editor = sharedPreference.edit()

                editor.putString("userPw", newPw)
                editor.putString("userMobile", newMobile)
                editor.putString("userNick", newNick)
                editor.putString("userHeight", height)
                editor.putString("userWeight", weight)

                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }



    }

    fun updateUser() {

    }

    fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}