package com.example.series_collector.di

import com.example.series_collector.utils.workers.SeriesWorker
import com.example.series_collector.utils.workers.SeriesWorkerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class WorkerModule {
    @Binds
    abstract fun bindSeriesWorker(impl: SeriesWorkerImpl): SeriesWorker
}
