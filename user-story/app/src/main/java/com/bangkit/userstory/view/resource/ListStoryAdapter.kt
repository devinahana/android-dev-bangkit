package com.bangkit.userstory.view.resource

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.userstory.data.response.Story
import com.bangkit.userstory.databinding.ItemStoryBinding
import com.bangkit.userstory.view.main.detail.DetailActivity
import com.bumptech.glide.Glide

class ListStoryAdapter<T> :
    ListAdapter<T, ListStoryAdapter.MyViewHolder<T>>(DIFF_CALLBACK as DiffUtil.ItemCallback<T>) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<T> {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            if (item is Story) {
                val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.KEY_ID, item.id)
                holder.itemView.context.startActivity(intentDetail)
            }
        }
    }

    class MyViewHolder<T>(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            if (item is Story) {
                binding.tvStoryOwner.text = item.name
                Glide.with(binding.root)
                    .load(item.photoUrl)
                    .into(binding.imgStoryPhoto)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }
}