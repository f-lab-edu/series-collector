package com.example.series_collector.data.repository

import com.example.series_collector.data.Series
import com.example.series_collector.data.SeriesFetcher
import com.example.series_collector.data.room.SeriesDao
import com.example.series_collector.data.source.FirestoreDataSource
import com.example.series_collector.utils.CATEGORY_FICTION
import com.example.series_collector.utils.CATEGORY_POPULAR
import com.example.series_collector.utils.CATEGORY_RECENT
import com.example.series_collector.utils.CATEGORY_TRAVEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val seriesDao: SeriesDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val seriesFetcher: SeriesFetcher
) {

    suspend fun getCategorys() = withContext(Dispatchers.IO) {
        firestoreDataSource.getCategorys()
    }

    suspend fun getSeriesByCategory(categoryId: Int): List<Series> = withContext(Dispatchers.IO) {
        val series = when (categoryId) {
            CATEGORY_RECENT -> {
                seriesDao.getRecentSeries()
            }
            CATEGORY_POPULAR -> {
                seriesDao.getPopularSeries()
            }
            CATEGORY_FICTION -> {
                seriesDao.getFictionSeries()
            }
            CATEGORY_TRAVEL -> {
                seriesDao.getTravelSeries()
            }
            else -> {
                seriesDao.getRecentSeries()
            }
        }

        fetchSeriesThumbnail(series)
    }

    private suspend fun fetchSeriesThumbnail(list: List<Series>): List<Series> =
        seriesFetcher.fetchSeriesThumbnail(list)


}