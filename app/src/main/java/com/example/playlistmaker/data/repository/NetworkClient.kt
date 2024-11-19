package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}