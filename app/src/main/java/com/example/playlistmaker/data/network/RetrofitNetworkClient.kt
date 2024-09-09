package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.repository.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// реализация интерфейса NetworkClient
class RetrofitNetworkClient : NetworkClient {

    // базовый URL для Retrofit
    private val baseUrlStr =
        "https://itunes.apple.com"  //https://itunes.apple.com/search?entity=song&term="мама"

    // подключаем библиотеку Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrlStr)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // получаем реализацию нашего com.example.playlistmaker.data.network.TrackApi
    private val trackApiService =
        retrofit.create(TrackApi::class.java) //val trackApiService = retrofit.create<TrackApi>()

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchRequest) {
                val resp = trackApiService.search(dto.str).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        } catch (ex: Exception) {
            return Response().apply { resultCode = 400 }
        }
        /*if (dto is TrackSearchRequest) {
            val resp = trackApiService.search(dto.str).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }*/
    }
}