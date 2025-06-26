package com.mj.remote.api

import com.mj.remote.model.SummaryBookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("books/v1/volumes")
    suspend fun getBookSearch(
        @Query("q") query: String,
        @Query("langRestrict") langRestrict: String = "kr",
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int = 20,
    ): SummaryBookResponse
}