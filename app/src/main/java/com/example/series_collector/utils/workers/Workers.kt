package com.example.series_collector.utils.workers

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface SeriesWork {
    fun updateSeriesStream(): Flow<WorkInfo>
}

class SeriesWorkImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : SeriesWork {

    private val workManager = WorkManager.getInstance(appContext)

    override fun updateSeriesStream(): Flow<WorkInfo> = requestUpdateWorker(workManager)

    private fun requestUpdateWorker(workManager: WorkManager): Flow<WorkInfo> {
        val request = OneTimeWorkRequestBuilder<SeriesUpdateWorker>()
            .addTag(SYNC_WORK_TAG)
            .build()
        workManager.beginUniqueWork("update_work", ExistingWorkPolicy.REPLACE, request).enqueue()

        return workManager.getWorkInfoByIdLiveData(request.id).asFlow()
    }


    companion object {
        const val SYNC_WORK_TAG = "SeriesSyncWorker"
    }


}