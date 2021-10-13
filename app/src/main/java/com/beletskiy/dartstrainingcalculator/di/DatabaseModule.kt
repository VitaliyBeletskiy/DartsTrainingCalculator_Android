package com.beletskiy.dartstrainingcalculator.di

import android.content.Context
import com.beletskiy.dartstrainingcalculator.data.DartsDatabase
import com.beletskiy.dartstrainingcalculator.data.SavedGameDao
import com.beletskiy.dartstrainingcalculator.data.SavedTossDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDartsDatabase(@ApplicationContext context: Context): DartsDatabase {
        return DartsDatabase.getInstance(context)
    }

    @Provides
    fun provideSavedGameDao(dartsDatabase: DartsDatabase): SavedGameDao {
        return dartsDatabase.savedGameDao()
    }

    @Provides
    fun provideSavedTossDao(dartsDatabase: DartsDatabase): SavedTossDao {
        return dartsDatabase.savedTossDao()
    }

}