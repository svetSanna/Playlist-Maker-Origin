package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.repository.NetworkClient

// реализация интерфейса NetworkClient
class RetrofitNetworkClient( private val trackApiService: TrackApi) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchRequest) {
                val resp = trackApiService.search(dto.str).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code()
                }
            }
            else {
                return Response().apply { resultCode = 400 }
            }
        }
        catch (ex: Exception) {
            return Response().apply { resultCode = 400 }
        }
    }
}