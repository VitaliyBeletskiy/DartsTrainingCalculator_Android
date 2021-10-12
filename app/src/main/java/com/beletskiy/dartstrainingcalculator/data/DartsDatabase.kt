package com.beletskiy.dartstrainingcalculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.beletskiy.dartstrainingcalculator.utils.DATABASE_NAME

@Database(entities = [SavedGame::class, SavedToss::class], version = 1, exportSchema = false)
abstract class DartsDatabase : RoomDatabase() {

    abstract fun savedGameDao(): SavedGameDao
    abstract fun savedTossDao(): SavedTossDao

    companion object {

        @Volatile
        private var instance: DartsDatabase? = null

        fun getInstance(context: Context): DartsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // FIXME: убрать .applicationContext
        private fun buildDatabase(context: Context): DartsDatabase {
            return Room.databaseBuilder(context.applicationContext, DartsDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

    }

}