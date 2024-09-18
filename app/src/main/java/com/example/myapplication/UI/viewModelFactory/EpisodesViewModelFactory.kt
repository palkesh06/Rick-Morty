package com.example.myapplication.UI.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Data.Repository.EpisodesRepository
import com.example.myapplication.Data.Retrofit.ApiInterface
import com.example.myapplication.Data.Retrofit.RetrofitInstance
import com.example.myapplication.UI.viewModel.EpisodesViewModel

class EpisodesViewModelFactory(
    private val api: ApiInterface = RetrofitInstance.api,
    private val repository: EpisodesRepository = EpisodesRepository(api)
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodesViewModel::class.java)) {
            return EpisodesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}