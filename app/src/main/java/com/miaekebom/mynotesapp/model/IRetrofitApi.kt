package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.LoginRequest
import com.miaekebom.mynotesapp.model.data.LoginResponse
import com.miaekebom.mynotesapp.model.data.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IRetrofitApi {

    companion object {

        fun create(authToken: String): IRetrofitApi {
        val httpClient = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
                chain.proceed(request)}
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .baseUrl("http://192.168.1.103:8081")
            .build()

        return retrofit.create(IRetrofitApi::class.java)
    }
}

    //user
    @GET("/get-all-users")
    fun getAllUsers(): Call<List<User>>

    @POST("/login-user")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/save-user")
    fun saveUser(@Body user: User): Call<User>

    @POST("/delete-user/{userId}")
    fun deleteUser(@Body userId: Int): Call<User>

    //list
    @GET("/get-lists/{ownerId}")
    fun getLists(@Path("ownerId") ownerId: Int): Call<List<com.miaekebom.mynotesapp.model.data.List>>

    @POST("/save-list/{ownerId}")
    fun saveList(@Path("ownerId") ownerId: Int, @Body list: com.miaekebom.mynotesapp.model.data.List): Call<Unit>

    @POST("/delete-list/{listId}")
    fun deleteList(@Path("listId") listId: Int): Call<Unit>

    @POST("/update-list-name/{listId}")
    fun updateList(@Path("listId") listId: Int, @Body list: com.miaekebom.mynotesapp.model.data.List): Call<Unit>

}