package com.example.drinksapp.di

//import dagger.hilt.android.components.ApplicationComponent
import android.content.Context
import androidx.room.Room
import com.example.drinksapp.data.local.AppDatabase
import com.example.drinksapp.data.remote.WebService
import com.example.drinksapp.utils.AppConstants.BASE_URL
import com.example.drinksapp.utils.AppConstants.DATABASE_NAME
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
//@InstallIn(ApplicationComponent::class)
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideCocktailDao(db: AppDatabase) = db.cocktailDao()


    //default value for retrofit connection ,read ,write timeout is 10 seconds
    @Singleton
    @Provides
    fun provideRetrofitInstance() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()



    @Singleton
    @Provides
    fun provideWebService(retrofit:Retrofit) = retrofit.create(WebService::class.java)
}