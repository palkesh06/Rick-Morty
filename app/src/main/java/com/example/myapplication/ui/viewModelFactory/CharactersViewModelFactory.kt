package com.example.myapplication.ui.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Data.Retrofit.ApiInterface
import com.example.myapplication.Data.Repository.CharactersRepository
import com.example.myapplication.Data.Retrofit.RetrofitInstance
import com.example.myapplication.ui.viewModel.CharactersViewModel

class CharactersViewModelFactory(
    private val api: ApiInterface = RetrofitInstance.api,
    private val repository: CharactersRepository = CharactersRepository(api)
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            return CharactersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}