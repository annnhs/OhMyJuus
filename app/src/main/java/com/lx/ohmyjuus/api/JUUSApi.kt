package com.lx.ohmyjuus.api

import com.lx.ohmyjuus.response.*
import com.lx.ohmyjuus.response.LoginRes
import com.lx.ohmyjuus.response.RegisterRes
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface JUUSApi {

    @FormUrlEncoded
    @POST("/juus/register")
    fun userRegister(
        @Field("userId") userId:String? = null,
        @Field("userPw") userPw:String? = null,
        @Field("userName") userName:String? = null,
        @Field("userMobile") userMobile:String? = null,
        @Field("userNick") userNick:String? = null,
        @Field("userBirth") userBirth:String? = null
    ): Call<RegisterRes>

    @GET("/juus/login")
    fun userLogin(
        @Query("userId") userId:String? = null,
        @Query("userPw") userPw:String? = null
    ): Call<LoginRes>

    @GET("/juus/save_upload")
    fun saveUpload(
        @Query("userId") userId:String? = null,
        @Query("filename") filename:String? = null
    ): Call<CaptureUploadRes>

//    @GET("/smokearea/list")
//    fun getSmokeArea(
//        @Query("point1") point1:String? = null,
//        @Query("point2") point2:String? = null,
//    ): Call<SmokeAreaResponse>
//
//    @GET("/smokearea/getpoint")
//    fun getPoint(
//        @Query("userid") userid:String? = null
//    ): Call<PointResponse>
//
//    @GET("/smokearea/updatePoint")
//    fun updatePoint(
//        @Query("userid") userid:String? = null,
//        @Query("total") total:Int? = null,
//        @Query("today") today:Int? = null,
//        @Query("totalSmoke") totalSmoke:Int? = null
//    ): Call<PointUpdateResponse>
//
//    @GET("/smokearea/report")
//    fun reportArea(
//        @Query("img") img:String? = null,
//        @Query("com") com:String? = null,
//        @Query("point") point:String? = null
//    ): Call<reportResponse>
/*
    @FormUrlEncoded
    @POST("/test1")
    fun postCustomerCreate(
        @Field("name") name:String? = null,
        @Field("age") age:String? = null,
        @Field("mobile") mobile:String? = null
    ): Call<CustomerResponse>

    @FormUrlEncoded
    @PUT("/test1/{id}")
    fun putCustomerUpdate(
        @Path("id") id:String? = null,
        @Field("name") name:String? = null,
        @Field("age") age:String? = null,
        @Field("mobile") mobile:String? = null
    ): Call<CustomerResponse>

    @DELETE("/test1/{id}")
    fun deleteCustomer(
        @Path("id") id:String? = null
    ):Call<CustomerDeleteResponse>
*/
}

class JUUSClient {

    // 붕어빵 틀에 진짜 변수상자나 함수 상자를 붙여두는 것 (CustomerClient.count 로 접근가능)
    companion object {

        private var instance: JUUSApi? = null

        val api: JUUSApi
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance(): JUUSApi {
            if (instance == null) {
                instance = create()
            }

            return instance as JUUSApi // 형변환
        }

        // 기본 URL
        private const val BASE_URL = "http://14.55.65.169"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): JUUSApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

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
                .create(JUUSApi::class.java)

        }

    }

}