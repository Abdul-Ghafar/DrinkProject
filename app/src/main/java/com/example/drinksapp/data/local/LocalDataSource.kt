package com.example.drinksapp.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.data.model.CocktailEntity
import com.example.drinksapp.data.model.asCocktailList
import com.example.drinksapp.data.model.asDrinkList
import com.example.drinksapp.data.model.asFavoriteEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocalDataSource @Inject constructor(private val cocktailDao: CocktailDao) {
    suspend fun saveFavoriteCocktail(cocktail: Cocktail) {
        return cocktailDao.saveFavoriteCocktail(cocktail.asFavoriteEntity())
    }

    fun getFavoritesCocktails(): LiveData<List<Cocktail>> {
        return cocktailDao.getAllFavoriteDrinksWithChanges().map { it.asDrinkList() }
    }

    suspend fun deleteCocktail(cocktail: Cocktail) {
        return cocktailDao.deleteFavoriteCoktail(cocktail.asFavoriteEntity())
    }

    suspend fun saveCocktail(cocktail: CocktailEntity) {
        cocktailDao.saveCocktail(cocktail)
    }

    suspend fun getCachedCocktails(cocktailName: String): Resource<List<Cocktail>> {
        return Resource.Success(cocktailDao.getCocktails(cocktailName).asCocktailList())
    }
    suspend fun getCachedCocktailsByFirstLetter(cocktailFirstLetter: String): Resource<List<Cocktail>> {
        return Resource.Success(cocktailDao.getCocktailsByFirstLetter(cocktailFirstLetter).asCocktailList())
    }

    suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean {
        return cocktailDao.getCocktailById(cocktail.cocktailId) != null
    }
}