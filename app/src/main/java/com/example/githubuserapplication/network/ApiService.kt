package com.example.githubuserapplication.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") q: String,
    ) : Call<UsersResponse>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String,
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>
}