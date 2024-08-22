package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.TrackRepository

// Задача этой реализации — получить список треков, используя сетевой клиент, и вернуть его в виде List<Track>:
class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(str: String): Resource<List<Track>> { //<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(str))

        if (response is TrackSearchResponse) {
            // данные получаем в формате TrackDto, следовательно надо сформировать данные в формате Track.
            // В принципе, можно сделать для этого mapper (в вебинаре это CurrencyRateMapper).
            // В теории было без mapper, так и сделала:
            val results = response.results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
            return Resource.Success(results) //results
        } else {
            return Resource.Error("Произошла сетевая ошибка")
        //return Resource.Error(R.string.network_err.toString())
        }

        /*if (response.resultCode == 200) {
             return (response as TrackSearchResponse).results.map {
                 Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
                     it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
             }
         } else {
             return emptyList()
         }*/
    }
}
