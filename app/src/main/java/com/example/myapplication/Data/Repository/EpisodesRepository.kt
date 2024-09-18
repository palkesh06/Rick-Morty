package com.example.myapplication.Data.Repository

import com.example.myapplication.Data.Retrofit.ApiInterface
import com.example.myapplication.Data.dataclass.episodes.Episodes

class EpisodesRepository(private val api: ApiInterface) {

    suspend fun getAllEpisodes(page: Int): Result<Episodes> {
        return try {
            val response = api.getAllEpisodes(page)
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