package com.example.githubuserapplication.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapplication.database.Favourite
import com.example.githubuserapplication.repository.FavouriteRepository

class FavouriteViewModel(application: Application) : ViewModel(){
    private val mFavouriteRepository: FavouriteRepository = FavouriteRepository(application)

    fun getAllFavourites(): LiveData<List<Favourite>> = mFavouriteRepository.getAllFavourites()
}