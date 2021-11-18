package com.lx.ohmyjuus.response


import com.google.gson.annotations.SerializedName

data class MapRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("longitude")
        val longitude: Double,
        @SerializedName("latitude")
        val latitude: Double
    )
}