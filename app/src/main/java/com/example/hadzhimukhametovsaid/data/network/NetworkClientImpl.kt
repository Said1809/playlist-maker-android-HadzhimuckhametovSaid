package com.example.hadzhimukhametovsaid.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.hadzhimukhametovsaid.data.dto.BaseResponse
import com.example.hadzhimukhametovsaid.data.dto.TracksSearchRequest
import com.example.hadzhimukhametovsaid.domain.api.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl(private val context: Context) : NetworkClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    override suspend fun doRequest(dto: Any): BaseResponse {
        if (!isConnected()) {
            return BaseResponse().apply { resultCode = -1 }
        }
        if (dto !is TracksSearchRequest) {
            return BaseResponse().apply { resultCode = 400 }
        }

        return try {
            val response = api.search(term = dto.expression)
            response.apply { resultCode = 200 }
        } catch (e: Exception) {
            BaseResponse().apply { resultCode = 500 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            ) return true
        }
        return false
    }

    companion object {
        private const val ITUNES_BASE_URL = "https://itunes.apple.com/"
    }
}
