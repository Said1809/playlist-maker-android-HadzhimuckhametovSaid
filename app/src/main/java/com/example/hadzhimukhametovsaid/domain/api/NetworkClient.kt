package com.example.hadzhimukhametovsaid.domain.api

import com.example.hadzhimukhametovsaid.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}
