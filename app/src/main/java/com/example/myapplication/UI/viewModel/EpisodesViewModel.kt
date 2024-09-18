package com.example.myapplication.UI.viewModel

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

    private fun loadEpisodes() {
        viewModelScope.launch {
            _loading.value = true
            val result: Result<Episodes?> = withContext(Dispatchers.IO) {
                repository.getAllEpisodes()
            }
            if (result.isSuccess ) {
                result.getOrNull()?.let {
                    _episodes.value = it
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }
            _loading.value = false
        }
    }
}