package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.LikeTrackDao
import com.example.playlistmaker.data.db.dao.PlayListDao
import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.data.db.entity.PlayListEntity

@Database(version = 2, entities = [LikeTrackEntity::class, PlayListEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeTrackDao(): LikeTrackDao
    abstract fun playlistDao(): PlayListDao
}