package com.example.githubuserapplication.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapplication.database.Favourite
import com.example.githubuserapplication.databinding.ItemRowProfileBinding
import com.example.githubuserapplication.helper.FavouriteDiffCallback

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.ListViewHolder>(){
    private val listFavourite = ArrayList<Favourite>()

    class ListViewHolder(var binding: ItemRowProfileBinding) : RecyclerView.ViewHolder(binding.root)

    fun setFavourites(data: List<Favourite>){
        val diffCallback = FavouriteDiffCallback(listFavourite, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFavourite.clear()
        listFavourite.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataFavourite = listFavourite[position]
        holder.binding.tvItemName.text = dataFavourite.username
        Glide.with(holder.itemView)
            .load(dataFavourite.avatarUrl)
            .circleCrop()
            .into(holder.binding.imgItemPhoto)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, dataFavourite.username)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listFavourite.size

}