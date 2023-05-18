package com.example.series_collector.utils.workers

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SeriesWorker {
    fun updateStream(): Flow<WorkInfo>
}

class SeriesWorkerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : SeriesWorker {

    private val workManager = WorkManager.getInstance(appContext)

    override fun updateStream(): Flow<WorkInfo> = updateSeriesStream(workManager)

    private fun updateSeriesStream(workManager: WorkManager): Flow<WorkInfo> {
        val request = startSeriesUpdateWorker()
        return workManager.getWorkInfoByIdLiveData(request.id).asFlow()
    }

    private fun startSeriesUpdateWorker() =
        getUpdateWorkerRequest().also {
            enqueueOneTimeWorker(workerName = "update_work", request =  it)
        }

    private fun enqueueOneTimeWorker(workerName: String, request: OneTimeWorkRequest) =
        workManager
            .beginUniqueWork(workerName, ExistingWorkPolicy.REPLACE, request)
            .enqueue()

    private fun getUpdateWorkerRequest() =
        OneTimeWorkRequestBuilder<SeriesUpdateWorker>()
            .build()

}
