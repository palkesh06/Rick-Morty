package com.example.myapplication.Data.Retrofit

import com.example.myapplication.Data.dataclass.characters.Characters
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("character")
    suspend fun getAllCharacters(): Response<Characters>
}