package com.lx.ohmyjuus.response


import com.google.gson.annotations.SerializedName

data class LoginRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("output")
    val output: List<Output>
) {
    data class Output(
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("user_pw")
        val userPw: String
    )
}