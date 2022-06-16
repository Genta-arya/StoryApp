package com.genta.storyapp.Data.API

import com.genta.storyapp.Data.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email : String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email : String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String,
        @Query("location")location: Int

    ) : Call<MsgGetStory>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token:String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): MsgGetStory

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<MsgGetStory>

    @Multipart
    @POST("stories")
    fun uploadStoryWithLocation(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double,
    ): Call<MsgGetStory>
}