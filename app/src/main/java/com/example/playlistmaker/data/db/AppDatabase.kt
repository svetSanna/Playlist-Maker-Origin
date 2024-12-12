package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.data.db.dao.LikeTrackDao
import com.example.playlistmaker.data.db.dao.PlayListDao
import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity


@Database(version = 2, entities = [LikeTrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeTrackDao(): LikeTrackDao
    abstract fun playListDao(): PlayListDao

   /* val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE play_list_entity")
        }
    }*/
}
