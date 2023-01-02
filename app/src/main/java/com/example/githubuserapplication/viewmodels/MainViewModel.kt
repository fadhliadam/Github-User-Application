package com.example.githubuserapplication.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuserapplication.network.ApiConfig
import com.example.githubuserapplication.network.UserItem
import com.example.githubuserapplication.network.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel(){
    private val _listUser = MutableLiveData<List<UserItem>>()
    val listUser: LiveData<List<UserItem>> = _listUser

    fun setSearch(username: String){
        val client = ApiConfig.getApiService().getListUsers(username)
        client.enqueue(object: Callback<UsersResponse>{
                override fun onResponse(
                    call: Call<UsersResponse>,
                    response: Response<UsersResponse>
                ) {
                    if(response.isSuccessful){
                        _listUser.postValue(response.body()?.items)
                    }
                    else{
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}