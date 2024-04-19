package com.varaha.markerapp.data.di

import android.content.Context
import androidx.room.Room
import com.varaha.markerapp.MyApplication
import com.varaha.markerapp.data.localdb.AppRoomDatabase
import com.varaha.markerapp.data.localdb.MarkerDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppRoomDatabase {
        return Room.databaseBuilder(
                    appContext,
                    AppRoomDatabase::class.java,
                    "MarkerRecordDatabase"
                ).fallbackToDestructiveMigration()
                    .build()
    }

    @Singleton
    @Provides
    fun provideMarkerDao(appDatabase: AppRoomDatabase): MarkerDataDao {
        return appDatabase.markerDataDao()
    }

    @Singleton
    @Provides
    fun exceptionHandler() : CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }
    }

    @Provides
    fun getApplication() : MyApplication {
        return MyApplication()
    }
}