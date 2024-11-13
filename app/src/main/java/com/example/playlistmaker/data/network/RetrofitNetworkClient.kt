package com.example.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.repository.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// реализация интерфейса NetworkClient
class RetrofitNetworkClient( private val trackApiService: TrackApi, private val context: Context) : NetworkClient {
   override suspend fun doRequest(dto: Any): Response {
       if (isConnected() == false) {
           return Response().apply { resultCode = -1 }
       }

       if (dto !is TrackSearchRequest) {
           return Response().apply { resultCode = 400 }
       }

       return withContext(Dispatchers.IO) {
           try {
               val response = trackApiService.search(dto.str)
               response.apply { resultCode = 200 }
           } catch (e: Throwable) {
               Response().apply { resultCode = 500 }
           }
       }
   }
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}