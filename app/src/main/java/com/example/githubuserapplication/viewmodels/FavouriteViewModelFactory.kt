package com.example.githubuserapplication.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavouriteViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FavouriteViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): FavouriteViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavouriteViewModelFactory::class.java) {
                    INSTANCE = FavouriteViewModelFactory(application)
                }
            }
            return INSTANCE as FavouriteViewModelFactory
        }
    }
}