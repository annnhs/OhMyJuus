package com.lx.ohmyjuus.response


import com.google.gson.annotations.SerializedName

data class CaptureUploadRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("output")
    val output: Output
) {
    data class Output(
        @SerializedName("fieldCount")
        val fieldCount: Int,
        @SerializedName("affectedRows")
        val affectedRows: Int,
        @SerializedName("insertId")
        val insertId: Int,
        @SerializedName("serverStatus")
        val serverStatus: Int,
        @SerializedName("warningCount")
        val warningCount: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("protocol41")
        val protocol41: Boolean,
        @SerializedName("changedRows")
        val changedRows: Int
    )
}