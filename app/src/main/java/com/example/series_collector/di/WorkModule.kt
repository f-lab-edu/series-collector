package com.example.series_collector.di

import com.example.series_collector.utils.workers.SeriesWork
import com.example.series_collector.utils.workers.SeriesWorkImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class WorkModule {
    @Binds
    abstract fun provideSeriesWork(seriesWork: SeriesWorkImpl): SeriesWork
}
