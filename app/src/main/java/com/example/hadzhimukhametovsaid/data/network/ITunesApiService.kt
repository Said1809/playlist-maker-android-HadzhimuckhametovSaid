package com.example.hadzhimukhametovsaid.data.network

import com.example.hadzhimukhametovsaid.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("entity") entity: String = "song"
    ): TracksSearchResponse
}
