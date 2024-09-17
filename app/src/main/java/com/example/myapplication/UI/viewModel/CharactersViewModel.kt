package com.example.myapplication.ui.theme.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Data.Repository.CharactersRepository
import com.example.myapplication.Data.dataclass.characters.Characters
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

    private fun loadCharacters() {
        viewModelScope.launch {
            _loading.value = true
            val result: Result<Characters?> = withContext(Dispatchers.IO) {
                repository.getAllCharacters()
            }
            if (result.isSuccess ) {
                result.getOrNull()?.let {
                    _characters.value = it
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }
            _loading.value = false
        }
    }
}