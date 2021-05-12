package com.dicoding.picodiploma.fundamentalsubfinal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.fundamentalsubfinal.model.FollowItems
import com.dicoding.picodiploma.fundamentalsubfinal.databinding.ItemRowFollowBinding

class FollowingAdapter(private val listFollow: ArrayList<FollowItems>) :
    RecyclerView.Adapter<FollowingAdapter.FollowViewHolder>() {

    fun setData(items: ArrayList<FollowItems>) {
        listFollow.clear()
        listFollow.addAll(items)
        notifyDataSetChanged()
    }

    inner class FollowViewHolder(private val binding: ItemRowFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userItems: FollowItems) {
            with(binding) {
                tvItemName.text = userItems.username
                Glide.with(imgItemAvatar.context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(65, 65))
                    .into(imgItemAvatar)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowViewHolder {
        val binding =
            ItemRowFollowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(listFollow[position])
    }

    override fun getItemCount(): Int = listFollow.size
}
