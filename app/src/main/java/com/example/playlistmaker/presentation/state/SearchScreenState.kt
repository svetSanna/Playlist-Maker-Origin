package com.example.playlistmaker.presentation.state

import com.example.playlistmaker.domain.entity.Track

sealed interface SearchScreenState {
    object Loading : SearchScreenState
    data class Content(val data: List<Track>) : SearchScreenState
    data class ContentHistory(val data: List<Track>): SearchScreenState
    data class Error(val message : Int): SearchScreenState
}
