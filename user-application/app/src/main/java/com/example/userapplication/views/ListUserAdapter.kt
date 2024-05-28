package com.example.userapplication.views

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.userapplication.data.local.entity.User
import com.example.userapplication.data.remote.response.UserResponse
import com.example.userapplication.databinding.ItemUserBinding
import com.example.userapplication.ui.detail.DetailActivity

class ListUserAdapter<T> :
    ListAdapter<T, ListUserAdapter.MyViewHolder<T>>(DIFF_CALLBACK as DiffUtil.ItemCallback<T>) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<T> {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            if (item is UserResponse) {
                intentDetail.putExtra(DetailActivity.KEY_USERNAME, item.login)
            } else if (item is User) {
                intentDetail.putExtra(DetailActivity.KEY_USERNAME, item.username)
            }
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder<T>(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            if (item is UserResponse) {
                binding.tvUserName.text = item.login
                Glide.with(binding.root)
                    .load(item.avatarUrl)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(binding.imgProfile)
            } else if (item is User) {
                binding.tvUserName.text = item.username
                Glide.with(binding.root)
                    .load(item.avatarUrl)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(binding.imgProfile)
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