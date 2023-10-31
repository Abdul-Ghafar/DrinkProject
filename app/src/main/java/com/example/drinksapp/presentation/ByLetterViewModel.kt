package com.example.drinksapp.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.di.ToastHelper
import com.example.drinksapp.domain.CocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ByLetterViewModel @Inject constructor(
    private val repository: CocktailRepository,
    private val toastHelper: ToastHelper,
    savedStateHandleLetter: SavedStateHandle
) : ViewModel() {

    val currentCocktailFirstLetter = savedStateHandleLetter.getLiveData<String>("cocktailFirstLetter", "a")

    fun setCocktailFirstLetter(cocktailFirstLetter: String) {
        currentCocktailFirstLetter.value = cocktailFirstLetter
    }

    val fetchCocktailListByLetter = currentCocktailFirstLetter.distinctUntilChanged().switchMap { cocktailFirst ->
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(Resource.Loading)
                try {
                    Log.d("DDDD", "isByLetter: fetchCocktailListByLetter ${currentCocktailFirstLetter.value}")
                    repository.getCocktailByFirstLetter(cocktailFirst).collect {
                        emit(it)
                    }
                } catch (e: Exception) {
                    Log.d("DDDD", "Exception: fetchCocktailListByLetter: "+e.message)
                    emit(Resource.Failure(e))
                }
            }
        }


    fun saveOrDeleteFavoriteCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            if (repository.isCocktailFavorite(cocktail)) {
                repository.deleteFavoriteCocktail(cocktail)
                toastHelper.sendToast("Cocktail deleted from favorites")
            } else {
                repository.saveFavoriteCocktail(cocktail)
                toastHelper.sendToast("Cocktail saved to favorites")
            }
        }
    }

    fun saveInFavoriteCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            if (!repository.isCocktailFavorite(cocktail)) {
                repository.saveFavoriteCocktail(cocktail)
                toastHelper.sendToast("Cocktail saved to favorites")
            }
        }
    }


    fun getFavoriteCocktails() =
        liveData<Resource<List<Cocktail>>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emitSource(repository.getFavoritesCocktails().map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun deleteFavoriteCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.deleteFavoriteCocktail(cocktail)
            toastHelper.sendToast("Cocktail deleted from favorites")
        }
    }

    suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean =
        repository.isCocktailFavorite(cocktail)
}