package com.example.hadzhimukhametovsaid.data.dto

import com.google.gson.annotations.SerializedName

data class TracksSearchResponse(
    @SerializedName("results") val results: List<TrackDto>?
) : BaseResponse()
