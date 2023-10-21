package com.example.drinksapp.data.remote

import com.example.drinksapp.data.model.CocktailList
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET("search.php")
    suspend fun getCocktailByName(@Query(value = "s") drinkName: String): CocktailList?

    @GET("search.php")
    suspend fun getCocktailByFirstLetter(@Query(value = "f") drinkName: String): CocktailList?
}