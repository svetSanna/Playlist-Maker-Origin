package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.data.db.dao.LikeTrackDao
import com.example.playlistmaker.data.db.dao.PlayListDao
import com.example.playlistmaker.data.db.dao.TrackInPlaylistDao
import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity


@Database(version = 3, entities = [LikeTrackEntity::class, PlaylistEntity::class,
            TrackInPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeTrackDao(): LikeTrackDao
    abstract fun playListDao(): PlayListDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao

   /* val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE play_list_entity")
        }
    }*/
}
