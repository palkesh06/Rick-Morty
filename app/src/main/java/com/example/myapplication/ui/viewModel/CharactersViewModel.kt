package com.example.myapplication.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Data.Repository.CharactersRepository
import com.example.myapplication.Data.dataclass.characters.Characters
import com.example.myapplication.Data.dataclass.episodes.Episodes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val repository: CharactersRepository) : ViewModel() {

    // LiveData to observe characters list
    private val _characters = MutableLiveData<Characters>()
    val characters: LiveData<Characters> get() = _characters

    // LiveData to observe loading status
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // LiveData to observe errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    init {
        loadCharacters()
    }

    var currentPage = 1
    var isLastPage = false

    fun loadCharacters() {
        if ( _loading.value == true || isLastPage) return
        viewModelScope.launch {
            _loading.value = true
            val result: Result<Characters?> = withContext(Dispatchers.IO) {
                repository.getAllCharacters(currentPage)
            }
            if (result.isSuccess ) {
                result.getOrNull()?.let { data ->
                    val currentList = _characters.value?.results?.toMutableList() ?: mutableListOf()
                    currentList.addAll(data.results)
                    _characters.value = Characters(
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