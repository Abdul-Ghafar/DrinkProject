package com.example.drinksapp.data.remote

import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.model.Cocktail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val webService: WebService
) {
    suspend fun getCocktailByName(cocktailName: String): Flow<Resource<List<Cocktail>>> =
        callbackFlow {
            this.trySend(
                Resource.Success(
                    webService.getCocktailByName(cocktailName)?.cocktailList ?: listOf()
                )
            ).isSuccess
            awaitClose { close() }
        }

    suspend fun getCocktailByFirstLetter(cocktailByFirstLetter: String): Flow<Resource<List<Cocktail>>> =
        callbackFlow {
            this.trySend(
                Resource.Success(
                    webService.getCocktailByFirstLetter(cocktailByFirstLetter)?.cocktailList ?: listOf()
                )
            ).isSuccess
            awaitClose { close() }
        }
}