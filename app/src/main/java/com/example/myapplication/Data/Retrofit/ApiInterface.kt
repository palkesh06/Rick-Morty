package com.example.myapplication.Data.Retrofit

import com.example.myapplication.Data.dataclass.characters.Characters
import com.example.myapplication.Data.dataclass.episodes.Episodes
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("character")
    suspend fun getAllCharacters(): Response<Characters>

    @GET("episode")
    suspend fun getAllEpisodes(): Response<Episodes>

}