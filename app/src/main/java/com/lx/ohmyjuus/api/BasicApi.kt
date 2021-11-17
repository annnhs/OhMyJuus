package com.lx.ohmyjuus.api

import com.lx.ohmyjuus.response.FileUploadResponse
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface BasicApi {

    @Multipart
    @POST("/profile/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part(value="params", encoding="UTF-8") params: HashMap<String,String> = hashMapOf()
    ): Call<FileUploadResponse>

}

class BasicClient {

    companion object {

        private var instance : BasicApi? = null

        val api: BasicApi
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance(): BasicApi {
            if (instance == null)
                instance = create()
            return instance as BasicApi
        }

        // 기본 URL
        private const val BASE_URL = "http://14.55.65.169"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): BasicApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-Client-Id", CLIENT_ID)
                    .addHeader("X-Client-Secret", CLIENT_SECRET)
                    .addHeader("X-Client-UserId", userId)
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(40, TimeUnit.SECONDS)  // 타임아웃 시간 설정 40초
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BasicApi::class.java)
        }

    }
}
