package com.example.drinksapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Cocktail(
    @SerializedName("idDrink")
    val cocktailId: String = "",
    @SerializedName("strDrinkThumb")
    val image: String = "",
    @SerializedName("strDrink")
    val name: String = "",
    @SerializedName("strInstructions")
    val description: String = "",
    @SerializedName("strAlcoholic")
    val hasAlcohol: String = "Non_Alcoholic"
) : Parcelable

data class CocktailList(
    @SerializedName("drinks")
    val cocktailList: List<Cocktail> = listOf()
)

@Entity(tableName = "cocktailTable")
data class CocktailEntity(
    @PrimaryKey
    val cocktailId: String,
    @ColumnInfo(name = "column_image")
    val image: String = "",
    @ColumnInfo(name = "column_name")
    val name: String = "",
    @ColumnInfo(name = "column_description")
    val description: String = "",
    @ColumnInfo(name = "column_has_alcohol")
    val hasAlcohol: String = "Non_Alcoholic"
)

@Entity(tableName = "favoritesTable")
data class FavoritesEntity(
    @PrimaryKey
    val cocktailId: String,
    @ColumnInfo(name = "column_image")
    val image: String = "",
    @ColumnInfo(name = "column_name")
    val name: String = "",
    @ColumnInfo(name = "column_description")
    val description: String = "",
    @ColumnInfo(name = "column_has_alcohol")
    val hasAlcohol: String = "Non_Alcoholic"
)

fun List<FavoritesEntity>.asDrinkList(): List<Cocktail> = this.map {
    Cocktail(it.cocktailId, it.image, it.name, it.description, it.hasAlcohol)
}

fun List<CocktailEntity>.asCocktailList(): List<Cocktail> = this.map {
    Cocktail(it.cocktailId, it.image, it.name, it.description, it.hasAlcohol)
}

fun Cocktail.asCocktailEntity(): CocktailEntity =
    CocktailEntity(this.cocktailId, this.image, this.name, this.description, this.hasAlcohol)

fun Cocktail.asFavoriteEntity(): FavoritesEntity =
    FavoritesEntity(this.cocktailId, this.image, this.name, this.description, this.hasAlcohol)