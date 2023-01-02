package com.example.githubuserapplication.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuserapplication.database.Favourite

class FavouriteDiffCallback(private val mOldFavouriteList: List<Favourite>, private val mNewFavouriteList: List<Favourite>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavouriteList.size
    }
    override fun getNewListSize(): Int {
        return mNewFavouriteList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavouriteList[oldItemPosition].id == mNewFavouriteList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavouriteList[oldItemPosition]
        val newEmployee = mNewFavouriteList[newItemPosition]
        return oldEmployee.username == newEmployee.username
    }
}