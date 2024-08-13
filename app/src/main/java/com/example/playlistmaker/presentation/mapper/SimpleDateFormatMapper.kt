package com.example.playlistmaker.presentation.mapper

import android.icu.text.SimpleDateFormat
import java.util.Locale

object SimpleDateFormatMapper {
    fun map(currentPosition : Int) : String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
    }
}