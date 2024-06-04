package com.bangkit.userstory.ui.resource

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.userstory.data.remote.response.Story
import com.bangkit.userstory.databinding.ItemStoryBinding
import com.bangkit.userstory.ui.main.detail.DetailActivity
import com.bumptech.glide.Glide

class ListStoryAdapter :
    PagingDataAdapter<Story, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }

        holder.itemView.setOnClickListener {
            if (item is Story) {
                val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.KEY_ID, item.id)
                holder.itemView.context.startActivity(intentDetail)
            }
        }
    }

    class MyViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {
            binding.tvStoryOwner.text = item.name
            Glide.with(binding.root)
                .load(item.photoUrl)
                .into(binding.imgStoryPhoto)
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}