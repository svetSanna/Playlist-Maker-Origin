package com.example.playlistmaker.data.network// интерфейс для работы с API  "iTunes Search API".
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
    @GET("/search?entity=song")
    // Аннотация @Query позволяет фильтровать наш запрос со значениями параметра.
    // Внутри неё мы должны указать ключ параметра для фильтрации  (term).
    suspend fun search(@Query("term") text: String): TrackSearchResponse
}