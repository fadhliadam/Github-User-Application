package com.example.githubuserapplication.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapplication.network.UserItem
import com.example.githubuserapplication.ui.DetailActivity.Companion.EXTRA_USERNAME
import com.example.githubuserapplication.databinding.ItemRowProfileBinding

class UserAdapter(private val listUser: ArrayList<UserItem>): RecyclerView.Adapter<UserAdapter.ListViewHolder>(){

    class ListViewHolder(var binding: ItemRowProfileBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(data: List<UserItem>){
        listUser.clear()
        listUser.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val(login,avatarUrl) = listUser[position]
        holder.binding.tvItemName.text = login
        Glide.with(holder.itemView)
            .load(avatarUrl)
            .circleCrop()
            .into(holder.binding.imgItemPhoto)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, login)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listUser.size

}