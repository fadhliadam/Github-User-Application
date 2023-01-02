package com.example.githubuserapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication.R
import com.example.githubuserapplication.databinding.FragmentFollowBinding
import com.example.githubuserapplication.network.UserItem
import com.example.githubuserapplication.viewmodels.DetailViewModel
import com.example.githubuserapplication.viewmodels.FavouriteViewModelFactory

class FollowingFragment : Fragment() {
    private lateinit var binding : FragmentFollowBinding
    private lateinit var followingAdapter: UserAdapter
    private lateinit var detailViewModel: DetailViewModel

    private var username: String? = null

    companion object {
        fun getInstance(login: String): Fragment {
            return FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString("USERNAME", login)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString("USERNAME")
        followingAdapter = UserAdapter(arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNoFound.setText(R.string.this_user_doesn_t_follow_anyone)
        showViewModel()
        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showViewModel(){
        val factory = FavouriteViewModelFactory.getInstance(requireActivity().application)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        detailViewModel.getFollowingUsers(username.toString())
        detailViewModel.followingData.observe(viewLifecycleOwner) { followingData ->
            if (followingData.isNotEmpty()) {
                showLoading(false)
                followingList(followingData)
            }else{
                binding.noFollowState.visibility = View.VISIBLE
            }
        }
    }

    private fun followingList(githubUsers: List<UserItem>) {
        followingAdapter.setData(githubUsers)
        binding.rvFollow.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.noFollowState.visibility = View.GONE
        binding.pbFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}