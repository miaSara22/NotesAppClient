package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
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
    fun getAllUsers(): Call<List<User>>

    @POST("/loginUser")
    @Headers("Content-Type: application/json")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/saveUser")
    @Headers("Content-Type: application/json")
    fun saveUser(@Body user: User): Call<RegisterResponse>

    @POST("/setUserImage/{userId}")
    fun setUserImage(@Path ("userId") userId: Int, @Body image: String): Call<ResponseBody>

    @POST("/deleteUserImage/{userId}")
    fun deleteUserImage(@Path("userId") userId: Int): Call<ResponseBody>

    @POST("/deleteUser/{userId}")
    fun deleteUser(@Body userId: Int): Call<Unit>

    //list
    @GET("/getLists/{ownerId}")
    fun getLists(@Path("ownerId") ownerId: Int): Call<List<com.miaekebom.mynotesapp.model.data.List>>

    @POST("/saveList/{ownerId}")
    fun saveList(@Path("ownerId") ownerId: Int, @Body list: com.miaekebom.mynotesapp.model.data.List): Call<Unit>

    @POST("/deleteList/{listId}")
    fun deleteList(@Path("listId") listId: Int): Call<Unit>

    @POST("/updateListName/{listId}")
    fun updateList(@Path("listId") listId: Int, @Body list: com.miaekebom.mynotesapp.model.data.List): Call<Unit>

    //note
    @POST("/saveNote/{ownerId}")
    fun saveNote(@Path("ownerId") ownerId: Int, @Body note: Note): Call<Unit>

    @POST("/deleteNote/{noteId}")
    fun deleteNote(@Path("noteId") noteId: Int): Call<Unit>

    @POST("/updateNote/{noteId}")
    fun updateNote(@Path("noteId") noteId: Int, @Body note: Note): Call<Unit>

    @GET("/getNotes/{ownerId}")
    fun getNotes(@Path("ownerId") ownerId: Int): Call<List<Note>>

}