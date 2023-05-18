package com.example.series_collector.ui.home

import androidx.lifecycle.*
import androidx.work.WorkInfo
import com.example.series_collector.data.Category
import com.example.series_collector.data.Series
import com.example.series_collector.data.repository.CategoryRepository
import com.example.series_collector.data.repository.SeriesRepository
import com.example.series_collector.utils.workers.SeriesWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val categoryRepository: CategoryRepository,
    private val seriesWork: SeriesWork,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _seriesContents = MutableLiveData<List<Category>>()
    val seriesContents: LiveData<List<Category>> = _seriesContents

    private val updateExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        when (throwable) {
            is Exception -> {
                _isLoading.postValue(false)
            }
        }
    }

    init {
        updateSeries()
    }

    fun updateSeries(){
        viewModelScope.launch(updateExceptionHandler) {
            _isLoading.value = true
            seriesWork.updateSeriesStream()
                .collect { workInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED
                        || workInfo.state == WorkInfo.State.FAILED
                    ) {
                        refreshSeriesContents()
                        _isLoading.value = false
                    }
                }
        }
    }

    private suspend fun refreshSeriesContents() {
        withContext(Dispatchers.IO) {
            val categorys: MutableList<Category> = categoryRepository.getCategorys()

            val seriesContents = categorys.map { category ->
                category.copy(seriesList = getSeriesByCategory(category.categoryId))
            }.toList()

            _seriesContents.postValue(seriesContents)
        }
    }


    private suspend fun getSeriesByCategory(categoryId: Int): List<Series> =
        categoryRepository.getSeriesByCategory(categoryId)


}

