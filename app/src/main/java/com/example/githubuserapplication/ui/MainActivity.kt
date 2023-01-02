package com.example.githubuserapplication.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication.R
import com.example.githubuserapplication.network.UserItem
import com.example.githubuserapplication.databinding.ActivityMainBinding
import com.example.githubuserapplication.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listUserAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listUserAdapter = UserAdapter(arrayListOf())
        binding.rvProfile.setHasFixedSize(true)

        binding.searchProfile.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    showLoading(true)
                    mainViewModel.setSearch(query)
                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.user_not_found), Toast.LENGTH_LONG)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewModel.listUser.observe(this) { githubUsers ->
            if(githubUsers.isNotEmpty()){
                setUsersData(githubUsers)
                showLoading(false)
            }
            else{
                binding.progressBar.visibility = View.GONE
                binding.noUsersState.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
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

    private fun setUsersData(githubUsers: List<UserItem>) {
        binding.rvProfile.layoutManager = if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            GridLayoutManager(this, 2)
        }else{
            LinearLayoutManager(this)
        }
        listUserAdapter.setData(githubUsers)
        binding.rvProfile.adapter = listUserAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.noUsersState.visibility = View.GONE
        binding.rvProfile.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}