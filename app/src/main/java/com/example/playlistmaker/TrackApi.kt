package com.example.playlistmaker// интерфейс для работы с API  "iTunes Search API".
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
    @GET("/search?entity=song")
    // Аннотация @Query позволяет фильтровать наш запрос со значениями параметра.
    // Внутри неё мы должны указать ключ параметра для фильтрации  (term).
    fun search(@Query("term") text: String): Call<TrackResponse>
}