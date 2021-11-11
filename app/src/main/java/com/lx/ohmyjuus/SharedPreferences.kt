package com.lx.ohmyjuus

import android.content.Context

object SharedPreferences {

//    private val MY_ACCOUNT : String = "account"

    fun setUserId(context: Context, input: String) {
        val prefs : android.content.SharedPreferences? = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString("userId", input)
        editor?.commit()
    }

    fun getUserId(context: Context): String {
        val prefs : android.content.SharedPreferences? = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return prefs?.getString("userId", "").toString()
    }

    fun setUserPass(context: Context, input: String) {
        val prefs : android.content.SharedPreferences? = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString("userPw", input)
        editor?.commit()
    }

    fun getUserPass(context: Context): String {
        val prefs : android.content.SharedPreferences? = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return prefs?.getString("userPw", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : android.content.SharedPreferences? = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.clear()
        editor?.commit()
    }
}