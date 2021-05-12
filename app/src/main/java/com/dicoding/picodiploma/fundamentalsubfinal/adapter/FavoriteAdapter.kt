package com.dicoding.picodiploma.fundamentalsubfinal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.fundamentalsubfinal.R
import com.dicoding.picodiploma.fundamentalsubfinal.databinding.ItemRowFavoriteBinding
import com.dicoding.picodiploma.fundamentalsubfinal.model.FavItems
import com.dicoding.picodiploma.fundamentalsubfinal.ui.DetailUserActivity

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<FavItems>()
        set(listFavorite) {
            this.listFavorite.clear()
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemRowFavoriteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(private val binding: ItemRowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favItems: FavItems) {
            with(binding) {
                tvItemName.text = favItems.username
                Glide.with(imgItemAvatar.context)
                    .load(favItems.avatar)
                    .apply(RequestOptions().override(85, 85))
                    .into(imgItemAvatar)
                tvCountFollowers.text = favItems.followers.toString()
                tvCountFollowing.text = favItems.following.toString()
                tvLoc.text = favItems.location.toString()
                tvComp.text = favItems.company.toString()

                ivShare.setOnClickListener {
                    val sIntent = Intent(Intent.ACTION_SEND)
                    sIntent.type = "text/plain"
                    val shareTitle =
                        activity.resources.getString(R.string.share_title, favItems.name.toString())
                    val shareSub = activity.resources.getString(
                        R.string.share_sub,
                        favItems.name.toString(),
                        favItems.username.toString()
                    )
                    sIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
                    activity.startActivity(Intent.createChooser(sIntent, shareTitle))
                }

                cardView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailUserActivity::class.java)
                                intent.putExtra(DetailUserActivity.EXTRA_USER, favItems.username)
                                intent.putExtra(DetailUserActivity.EXTRA_ID, favItems.id)
                                activity.startActivity(intent)
                            }
                        })
                )
            }
        }
    }
}