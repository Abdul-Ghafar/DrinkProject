package com.example.drinksapp.domain

import androidx.lifecycle.LiveData
import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.data.model.CocktailEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Gastón Saillén on 16 July 2020
 */
interface CocktailRepository {
    suspend fun getCocktailByName(cocktailName: String): Flow<Resource<List<Cocktail>>>
    suspend fun getCocktailByFirstLetter(cocktailFirstLetter: String): Flow<Resource<List<Cocktail>>>
    suspend fun saveFavoriteCocktail(cocktail: Cocktail)
    suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean
    suspend fun getCachedCocktails(cocktailName: String): Resource<List<Cocktail>>
    suspend fun getCachedCocktailsByFirstLetter(cocktailFirstAlpha: String): Resource<List<Cocktail>>
    suspend fun saveCocktail(cocktail: CocktailEntity)
    fun getFavoritesCocktails(): LiveData<List<Cocktail>>
    suspend fun deleteFavoriteCocktail(cocktail: Cocktail)

}