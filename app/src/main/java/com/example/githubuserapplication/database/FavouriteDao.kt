package com.example.githubuserapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favourite: Favourite)

    @Delete
    fun delete(favourite: Favourite)

    @Query("Select * from favourite ORDER by id ASC")
    fun getAllFavourites(): LiveData<List<Favourite>>
}