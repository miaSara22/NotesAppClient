package com.miaekebom.mynotesapp.model.data

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

class ApiResponseHitsList {

    data class ApiResponse(@SerializedName("hits") val imagesList: List<ApiImage>)
    data class ApiImage(@SerializedName("webformatURL") val imagePath: String)
}