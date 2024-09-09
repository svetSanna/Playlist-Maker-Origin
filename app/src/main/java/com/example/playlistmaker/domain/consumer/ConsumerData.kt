package com.example.playlistmaker.domain.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val code: Int) : ConsumerData<T> //(val message: String) : ConsumerData<T> //p7
}