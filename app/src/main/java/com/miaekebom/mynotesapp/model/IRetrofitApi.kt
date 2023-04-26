package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface IRetrofitApi {

    companion object {

        fun create(authToken: String): IRetrofitApi {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $authToken")
                        .build()
                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .baseUrl("http://192.168.1.103:9000")
            .build()

        return retrofit.create(IRetrofitApi::class.java)
    }
}

    //user
    @GET("/getAllUsers")
    @Headers("Content-Type: application/json")
    suspend fun getAllUsers(@Header("Authorization") authToken: String): List<User>

    @POST("/loginUser")
    @Headers("Content-Type: application/json")
    suspend fun loginUser(@Body loginRequest: LoginRequest, @Header("Authorization") authToken: String? = null): LoginResponse

    @POST("/saveUser")
    @Headers("Content-Type: application/json")
    suspend fun saveUser(@Body user: User, @Header("Authorization") authToken: String? = null): com.miaekebom.mynotesapp.model.data.Response

    @POST("/setUserImage/{userId}")
    suspend fun setUserImage(@Path ("userId") userId: Int, @Body image: String): Call<ResponseBody>

    @POST("/deleteUserImage/{userId}")
    suspend fun deleteUserImage(@Path("userId") userId: Int): Call<ResponseBody>

    @POST("/deleteUser/{userId}")
    @Headers("Content-Type: application/json")
    suspend fun deleteUser(@Body userId: Int, @Header("Authorization") authToken: String): Response<Unit>

    //list
    @GET("/getLists/{ownerId}")
    @Headers("Content-Type: application/json")
    suspend fun getLists(@Path("ownerId") ownerId: Int, @Header("Authorization") authToken: String): Response<List<com.miaekebom.mynotesapp.model.data.List>>

    @POST("/saveList")
    @Headers("Content-Type: application/json")
    suspend fun saveList(@Body list: com.miaekebom.mynotesapp.model.data.List, @Header("Authorization") authToken: String): com.miaekebom.mynotesapp.model.data.Response

    @POST("/deleteList/{listId}")
    @Headers("Content-Type: application/json")
    suspend fun deleteList(@Path("listId") listId: Int, @Header("Authorization") authToken: String): Response<Unit>

    @POST("/updateListName/{listId}")
    @Headers("Content-Type: application/json")
    suspend fun updateList(@Path("listId") listId: Int, @Body list: com.miaekebom.mynotesapp.model.data.List, @Header("Authorization") authToken: String): Response<Unit>

    //note
    @POST("/saveNote/{ownerId}")
    @Headers("Content-Type: application/json")
    suspend fun saveNote(@Path("ownerId") ownerId: Int, @Body note: Note, @Header("Authorization") authToken: String): Response<Unit>

    @POST("/deleteNote/{noteId}")
    @Headers("Content-Type: application/json")
    suspend fun deleteNote(@Path("noteId") noteId: Int, @Header("Authorization") authToken: String): Response<Unit>

    @POST("/updateNote/{noteId}")
    @Headers("Content-Type: application/json")
    suspend fun updateNote(@Path("noteId") noteId: Int, @Body note: Note, @Header("Authorization") authToken: String): Response<Unit>

    @GET("/getNotes/{ownerId}")
    @Headers("Content-Type: application/json")
    suspend fun getNotes(@Path("ownerId") ownerId: Int, @Header("Authorization") authToken: String): Response<List<Note>>

}