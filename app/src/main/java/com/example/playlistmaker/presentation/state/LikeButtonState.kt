package com.example.playlistmaker.presentation.state

// состояние: трек находится в избранном или нет. следовательно нопочка лайк меняется
sealed interface LikeButtonState {
    object Liked : LikeButtonState
    object Unliked : LikeButtonState
}