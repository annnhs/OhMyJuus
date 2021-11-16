package com.lx.ohmyjuus.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("output")
    val output: Output
) {
    data class Output(
        @SerializedName("filename")
        val filename: String
    )
}