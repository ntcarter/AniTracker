package com.ntc.anitracker.ui.adapters.galleryadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.anime.Anime
import com.ntc.anitracker.api.models.topanime.TopA
import com.ntc.anitracker.databinding.ItemGalleryBinding

private const val TAG = "GalleryAnimeAdapter"

class GalleryAnimeAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<TopA, GalleryAnimeAdapter.TopAnimeViewHolder>(TOP_ANIME_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAnimeViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopAnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopAnimeViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    interface OnItemClickListener {
        fun onCoverClick(anime: TopA)
    }

    inner class TopAnimeViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(top: TopA) {
            binding.apply {
                Glide.with(itemView)
                    .load(top.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageCover)

                imageCover.setOnClickListener {
                    listener.onCoverClick(top)
                }

                titleText.text = top.title
                tvScore.text = top.score.toString()
            }
        }
    }

    companion object {
        private val TOP_ANIME_COMPARATOR = object : DiffUtil.ItemCallback<TopA>() {
            override fun areItemsTheSame(oldItem: TopA, newItem: TopA) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: TopA, newItem: TopA) = oldItem == newItem
        }
    }
}