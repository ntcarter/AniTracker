package com.ntc.anitracker.ui.adapters.galleryadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.topmanga.TopM
import com.ntc.anitracker.databinding.ItemGalleryBinding

private const val TAG = "GalleryAnimeAdapter"

class GalleryMangaAdapter(
    private val listener: OnItemClickListener
) :
    PagingDataAdapter<TopM, GalleryMangaAdapter.TopMangaViewHolder>(TOP_MANGA_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMangaViewHolder {
        Log.d(TAG, "onCreateViewHolder: CREATING VIEWHOLDER")
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopMangaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopMangaViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    interface OnItemClickListener {}

    inner class TopMangaViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(top: TopM) {
            binding.apply {
                Glide.with(itemView)
                    .load(top.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageCover)

                titleText.text = top.title
                titleText.text = top.title
                tvScore.text = top.score.toString()
            }
        }
    }

    companion object {
        private val TOP_MANGA_COMPARATOR = object : DiffUtil.ItemCallback<TopM>() {
            override fun areItemsTheSame(oldItem: TopM, newItem: TopM) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: TopM, newItem: TopM) = oldItem == newItem
        }
    }
}