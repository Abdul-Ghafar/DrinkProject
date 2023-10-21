package com.example.drinksapp.domain

import androidx.lifecycle.LiveData
import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.local.LocalDataSource
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.data.model.CocktailEntity
import com.example.drinksapp.data.model.asCocktailEntity
import com.example.drinksapp.data.remote.NetworkDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject



@ExperimentalCoroutinesApi
@ActivityRetainedScoped
class DefaultCocktailRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : CocktailRepository {

    override suspend fun getCocktailByName(cocktailName: String): Flow<Resource<List<Cocktail>>> =
        callbackFlow {

            this.trySend(getCachedCocktails(cocktailName)).isSuccess

            networkDataSource.getCocktailByName(cocktailName).collect {
                when (it) {
                    is Resource.Success -> {
                        for (cocktail in it.data) {
                            saveCocktail(cocktail.asCocktailEntity())
                        }
                        this.trySend(getCachedCocktails(cocktailName)).isSuccess
                    }
                    is Resource.Failure -> {
                        this.trySend(getCachedCocktails(cocktailName)).isSuccess
                    }
                }
            }
            awaitClose { cancel() }
        }



    override suspend fun getCocktailByFirstLetter(cocktailFirstLetter: String): Flow<Resource<List<Cocktail>>> =
        callbackFlow {

            this.trySend(getCachedCocktailsByFirstLetter(cocktailFirstLetter)).isSuccess

            networkDataSource.getCocktailByFirstLetter(cocktailFirstLetter).collect {
                when (it) {
                    is Resource.Success -> {

                        for (cocktail in it.data) {
                            saveCocktail(cocktail.asCocktailEntity())
                        }
                        this.trySend(getCachedCocktailsByFirstLetter(cocktailFirstLetter)).isSuccess
                    }
                    is Resource.Failure -> {
                        this.trySend(getCachedCocktailsByFirstLetter(cocktailFirstLetter)).isSuccess
                    }
                }
            }
            awaitClose { cancel() }
        }


    override suspend fun saveFavoriteCocktail(cocktail: Cocktail) {
        localDataSource.saveFavoriteCocktail(cocktail)
    }

    override suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean =
        localDataSource.isCocktailFavorite(cocktail)

    override suspend fun saveCocktail(cocktail: CocktailEntity) {
        localDataSource.saveCocktail(cocktail)
    }

    override fun getFavoritesCocktails(): LiveData<List<Cocktail>> {
        return localDataSource.getFavoritesCocktails()
    }

    override suspend fun deleteFavoriteCocktail(cocktail: Cocktail) {
        localDataSource.deleteCocktail(cocktail)
    }

    override suspend fun getCachedCocktails(cocktailName: String): Resource<List<Cocktail>> {
        return localDataSource.getCachedCocktails(cocktailName)
    }

    override suspend fun getCachedCocktailsByFirstLetter(cocktailFirstLetter: String): Resource<List<Cocktail>> {
        return localDataSource.getCachedCocktailsByFirstLetter(cocktailFirstLetter)
    }
}