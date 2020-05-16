package com.crp.mumbaiinsider.network

import com.crp.mumbaiinsider.model.MainResponse
import retrofit2.http.GET

interface InsiderAPI {
    @GET("home?norm=1&filterBy=go-out&city=mumbai")
    suspend fun getApiResponse():MainResponse
}