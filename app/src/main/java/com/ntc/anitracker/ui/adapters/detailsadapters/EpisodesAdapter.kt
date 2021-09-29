package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ntc.anitracker.api.models.episode.Episode
import com.ntc.anitracker.databinding.ItemEpisodeBinding

private const val TAG = "EpisodesAdapter"

class EpisodesAdapter(var titleTextColor: Int, var bodyTextColor: Int) :
    PagingDataAdapter<Episode, EpisodesAdapter.EpisodeViewHolder>(TOP_EPISODE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Episode) {
            binding.apply {
                tvEpisodeNumber.text = currentItem.episode_id.toString()
                tvEpisodeTitle.text = currentItem.title
                tvEpisodeAiredDate.text = currentItem.aired?.substring(0..9) ?: ""
                tvEpisodeKanji.text = currentItem.title_japanese
                tvEpisodeRomanji.text = currentItem.title_romanji

                tvEpisodeNumber.setTextColor(titleTextColor)
                tvEpisodeTitle.setTextColor(titleTextColor)
                tvEpisodeAiredDate.setTextColor(titleTextColor)
                tvEpisodeKanji.setTextColor(bodyTextColor)
                tvEpisodeRomanji.setTextColor(bodyTextColor)
            }
        }
    }

    companion object {
        private val TOP_EPISODE_COMPARATOR = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode) =
                oldItem.video_url == newItem.video_url

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode) = oldItem == newItem
        }
    }
}