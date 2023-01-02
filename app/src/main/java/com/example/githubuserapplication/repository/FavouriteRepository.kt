package com.example.githubuserapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserapplication.database.Favourite
import com.example.githubuserapplication.database.FavouriteDao
import com.example.githubuserapplication.database.FavouriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavouriteRepository(application: Application) {
    private val mFavouritesDao: FavouriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavouriteDatabase.getDatabase(application)
        mFavouritesDao = db.favouriteDao()
    }
    fun getAllFavourites(): LiveData<List<Favourite>> = mFavouritesDao.getAllFavourites()
    fun insert(favourite: Favourite) {
        executorService.execute { mFavouritesDao.insert(favourite) }
    }
    fun delete(favourite: Favourite) {
        executorService.execute { mFavouritesDao.delete(favourite) }
    }
}