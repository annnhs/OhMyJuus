package com.lx.ohmyjuus.response


import com.google.gson.annotations.SerializedName

data class SmokeAreaResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: ArrayList<Data>
) {
    data class Data(
        @SerializedName("ROWNUM")
        val ROWNUM: Int,
        @SerializedName("smoking_id")
        val smokingId: Int,
        @SerializedName("smoking_name")
        val smokingName: String,
        @SerializedName("smoking_address")
        val smokingAddress: String,
        @SerializedName("longitude")
        val longitude: Double,
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("distance")
        val distance: Double
    )
}