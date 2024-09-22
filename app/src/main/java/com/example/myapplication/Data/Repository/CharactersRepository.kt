package com.example.myapplication.Data.Repository

import com.example.myapplication.Data.Retrofit.ApiInterface
import com.example.myapplication.Data.dataclass.characters.Characters

class CharactersRepository(private val api: ApiInterface) {

    suspend fun getAllCharacters(page : Int) : Result<Characters>{
         return try {
             val response = api.getAllCharacters(page)
             if (response.isSuccessful) {
                 val body = response.body()
                 if (body != null) {
                     Result.success(body)
                 } else {
                     Result.failure(Exception("Error: Response body is null"))
                 }
             } else {
                 Result.failure(Exception("Error: ${response.code()}"))
             }
         } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message}"))
        }
    }
}