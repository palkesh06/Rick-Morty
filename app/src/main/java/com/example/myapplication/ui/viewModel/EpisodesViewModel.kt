package com.example.myapplication.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Data.Repository.EpisodesRepository
import com.example.myapplication.Data.dataclass.episodes.Episodes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodesViewModel(private val repository: EpisodesRepository) : ViewModel() {

    // LiveData to observe characters list
    private val _episodes = MutableLiveData<Episodes>()
    val episodes: LiveData<Episodes> get() = _episodes

    // LiveData to observe loading status
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // LiveData to observe errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        loadEpisodes()
    }

    var currentPage = 1
    var isLastPage = false

    fun loadEpisodes() {
        if ( _loading.value == true || isLastPage) return
        viewModelScope.launch {
            _loading.value = true
            val result: Result<Episodes?> = withContext(Dispatchers.IO) {
                repository.getAllEpisodes(currentPage)
            }
            if (result.isSuccess ) {
                result.getOrNull()?.let { data ->
                    val currentList = _episodes.value?.results?.toMutableList() ?: mutableListOf()
                    currentList.addAll(data.results)
                    _episodes.value = Episodes(
                        data.info,
                        currentList
                    )
                    isLastPage =  currentPage == data.info.pages
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }
            _loading.value = false
            currentPage++
        }
    }
}