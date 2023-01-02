package com.example.githubuserapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapplication.database.Favourite
import com.example.githubuserapplication.network.ApiConfig
import com.example.githubuserapplication.network.DetailResponse
import com.example.githubuserapplication.network.UserItem
import com.example.githubuserapplication.repository.FavouriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (mApplication: Application): ViewModel() {
    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser : LiveData<DetailResponse> = _detailUser

    private val _followersData = MutableLiveData<List<UserItem>>()
    val followersData : LiveData<List<UserItem>> = _followersData

    private val _followingData = MutableLiveData<List<UserItem>>()
    val followingData : LiveData<List<UserItem>> = _followingData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val mFavoriteUserRepository: FavouriteRepository =
        FavouriteRepository(mApplication)

    fun getAllFavorites(): LiveData<List<Favourite>> = mFavoriteUserRepository.getAllFavourites()

    fun insertData(user: Favourite){

        mFavoriteUserRepository.insert(user)

    }

    fun delete(user: Favourite){

        mFavoriteUserRepository.delete(user)

    }

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun getDetailUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object: Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _detailUser.postValue(response.body())
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowerUsers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object: Callback<List<UserItem>>{
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _followersData.postValue(response.body())
                }
                else{
                    Log.e(TAG, "responseFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "responseFailure: ${t.message}")
            }

        })
    }

    fun getFollowingUsers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object: Callback<List<UserItem>>{
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _followingData.postValue(response.body())
                }
                else{
                    Log.e(TAG, "responseFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "responseFailure: ${t.message}")
            }

        })
    }
}