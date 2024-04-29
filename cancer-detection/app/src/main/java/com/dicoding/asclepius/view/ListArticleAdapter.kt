package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.api.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemArticleBinding

class ListArticleAdapter<T> :
    ListAdapter<T, ListArticleAdapter.MyViewHolder<T>>(DIFF_CALLBACK as DiffUtil.ItemCallback<T>) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<T> {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            if (item is ArticlesItem) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    class MyViewHolder<T>(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T) {
            if (item is ArticlesItem) {
                binding.tvTitle.text = item.title
                binding.tvDescription.text = item.description
                Glide.with(binding.root)
                    .load(item.urlToImage)
                    .into(binding.imgArticle)
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