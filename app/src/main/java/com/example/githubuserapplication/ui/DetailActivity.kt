package com.example.githubuserapplication.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserapplication.network.DetailResponse
import com.example.githubuserapplication.R
import com.example.githubuserapplication.database.Favourite
import com.example.githubuserapplication.databinding.ActivityDetailBinding
import com.example.githubuserapplication.viewmodels.DetailViewModel
import com.example.githubuserapplication.viewmodels.FavouriteViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    private var favourite: Favourite? = null
    private var buttonFavourite: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        detailViewModel = obtainViewModel(this@DetailActivity)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        val unfavouriteColor = binding.btnFavourite.imageTintList
        val favouriteColor = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.red)
        )

        detailViewModel.getDetailUser(username.toString())
        detailViewModel.detailUser.observe(this) { detailUser ->
            if (detailUser != null) {
                setUserData(detailUser)
                favourite = Favourite(detailUser.id, detailUser.login, detailUser.avatarUrl)
                detailViewModel.getAllFavorites().observe(this){ listFavourites ->
                    if(listFavourites !== null){
                        for(user in listFavourites){
                            if(detailUser.id == user.id){
                                buttonFavourite = true
                                binding.btnFavourite.imageTintList = favouriteColor
                            }
                        }
                    }
                }
                binding.btnFavourite.setOnClickListener {
                    if(buttonFavourite == false){
                        insertUserToDatabase(detailUser)
                        binding.btnFavourite.imageTintList = favouriteColor
                        buttonFavourite = true
                    } else
                    {
                        detailViewModel.delete(favourite as Favourite)
                        Toast.makeText(this@DetailActivity,
                            "${detailUser.login} removed from favourites!",
                            Toast.LENGTH_SHORT).show()
                        binding.btnFavourite.imageTintList = unfavouriteColor
                        buttonFavourite = false
                    }
                }
                showLoading(false)
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username.toString())
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun setUserData(user: DetailResponse?) {
        Glide.with(this@DetailActivity)
            .load(user?.avatarUrl)
            .circleCrop()
            .into(binding.avatarPhoto)

        binding.apply {
            if(user?.name != null){
                tvName.visibility = View.VISIBLE
                tvName.text = user.name
            }else{
                tvName.visibility = View.GONE
            }
            if(user?.company != null){
                iconCompany.visibility = View.VISIBLE
                tvCompanyName.visibility = View.VISIBLE
                tvCompanyName.text = user.company
            }else {
                iconCompany.visibility = View.GONE
                tvCompanyName.visibility = View.GONE
            }
            if(user?.location != null){
                iconLocation.visibility = View.VISIBLE
                tvLocationName.visibility = View.VISIBLE
                tvLocationName.text = user.location
            }else{
                iconLocation.visibility = View.GONE
                tvLocationName.visibility = View.GONE
            }
            tvUsername.text = user?.login
            tvRepoNumber.text = user?.publicRepos.toString()
            tvFollowersNumber.text = user?.followers.toString()
            tvFollowingNumber.text = user?.following.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentHider.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.pbProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(detailUsersActivity: AppCompatActivity): DetailViewModel {

        val factory = FavouriteViewModelFactory.getInstance(detailUsersActivity.application)
        return ViewModelProvider(detailUsersActivity, factory)[DetailViewModel::class.java]

    }

    private fun insertUserToDatabase(detailList: DetailResponse){
        favourite.let { favourite ->
            favourite?.id = detailList.id
            favourite?.username = detailList.login
            favourite?.avatarUrl = detailList.avatarUrl
            detailViewModel.insertData(favourite as Favourite)
            Toast.makeText(this@DetailActivity,
                "${detailList.login} added to favourites",
                Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

}