package com.miaekebom.mynotesapp.utils

import android.util.Log
import android.widget.ImageButton
import androidx.lifecycle.viewModelScope
import com.miaekebom.mynotesapp.model.IPixabayApi
import com.miaekebom.mynotesapp.model.data.ApiResponseHitsList
import com.miaekebom.mynotesapp.model.data.User
import com.miaekebom.mynotesapp.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ImagesManager {

    fun getImageFromApi(user: User, userProfile: ImageButton, mainViewModel: MainViewModel) {
        val retrofit = IPixabayApi.create()
        retrofit.getImages().enqueue(object : Callback<ApiResponseHitsList.ApiResponse> {

            override fun onResponse(
                call: Call<ApiResponseHitsList.ApiResponse>,
                response: Response<ApiResponseHitsList.ApiResponse>
            ) {

                val apiResponse = response.body()
                val i = (0 until apiResponse!!.imagesList.size).random()
                val apiImage = apiResponse.imagesList[i]
                Picasso.get().load(apiImage.imagePath).into(userProfile)

                mainViewModel.viewModelScope.launch(Dispatchers.IO) {
                    mainViewModel.updateUserImage(user, apiImage.imagePath)
                }
            }

            override fun onFailure(call: Call<ApiResponseHitsList.ApiResponse>, t: Throwable) {
                Log.e("Wrong api response", t.message.toString())
            }
        })
    }
}
