package com.example.githubuserapplication.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication.R
import com.example.githubuserapplication.databinding.ActivityFavouriteUsersBinding
import com.example.githubuserapplication.viewmodels.FavouriteViewModel
import com.example.githubuserapplication.viewmodels.FavouriteViewModelFactory

class FavouriteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteUsersBinding
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_users)
        binding = ActivityFavouriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favouriteViewModel = obtainViewModel(this@FavouriteUsersActivity)

        binding.rvFavourite.setHasFixedSize(true)
        favouriteAdapter = FavouriteAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavourite.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavourite.addItemDecoration(itemDecoration)
        setListLayout()

        favouriteViewModel.getAllFavourites().observe(this) { favouriteList ->
            if (favouriteList.isEmpty()) {
                binding.apply {
                    noFavouritesState.visibility = View.VISIBLE
                    rvFavourite.visibility = View.GONE
                }
            } else{
                favouriteAdapter.setFavourites(favouriteList)
                binding.apply {
                    noFavouritesState.visibility = View.GONE
                    rvFavourite.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.preferences_menu -> {
                val i = Intent(this, PreferenceActivity::class.java)
                startActivity(i)
            }
            R.id.favourite_menu -> {
                val i = Intent(this, FavouriteUsersActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListLayout(){
        binding.rvFavourite.layoutManager = if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            GridLayoutManager(this, 2)
        }else{
            LinearLayoutManager(this)
        }
        binding.rvFavourite.setHasFixedSize(false)
        binding.rvFavourite.adapter = favouriteAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavouriteViewModel {
        val factory = FavouriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavouriteViewModel::class.java]
    }
}