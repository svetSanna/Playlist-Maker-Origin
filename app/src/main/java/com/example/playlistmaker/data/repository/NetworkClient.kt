package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}