package com.varaha.markerapp.data.di

import com.varaha.markerapp.data.repositoryImpl.HomeRepositoryImpl
import com.varaha.markerapp.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ViewModelComponent {

    @Binds
    abstract fun provideMarkerDetailRepository(markerDetailsRepositoryImpl: HomeRepositoryImpl) : HomeRepository
}