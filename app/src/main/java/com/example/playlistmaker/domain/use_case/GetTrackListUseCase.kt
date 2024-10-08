package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import java.util.concurrent.Executors

class GetTrackListUseCase(private val repository: TrackRepository) {

    private val executor = Executors.newSingleThreadExecutor()

    operator fun invoke(str: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            val trackResponse = repository.searchTrack(str)
            when (trackResponse) {
                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(trackResponse.data))
                }

                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(-1))
                }
            }
        }
    }
}

