package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.LikeTrackDao
import com.example.playlistmaker.data.db.entity.LikeTrackEntity

@Database(version = 1, entities = [LikeTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeTrackDao(): LikeTrackDao
}