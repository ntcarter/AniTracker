package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ntc.anitracker.api.models.anime.CombinedRelated
import com.ntc.anitracker.databinding.ItemRelatedBinding

class RelatedAdapter(
    private val relatedList: List<CombinedRelated>,
    val titleTextColor: Int,
    val listener: OnRelatedClick
) : RecyclerView.Adapter<RelatedAdapter.RelatedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedViewHolder {
        val binding =
            ItemRelatedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RelatedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RelatedViewHolder, position: Int) {
        val currentItem = relatedList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = relatedList.size

    interface OnRelatedClick {
        fun onRelatedAnimeClick(malId: Int)
        fun onRelatedMangaClick(malId: Int)
    }

    inner class RelatedViewHolder(private val binding: ItemRelatedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: CombinedRelated) {
            binding.apply {

                root.setOnClickListener {
                    if (currentItem.animeType == "anime") {
                        listener.onRelatedAnimeClick(currentItem.mal_id)
                    } else if (currentItem.animeType == "manga") {
                        listener.onRelatedMangaClick(currentItem.mal_id)
                    }
                }

                val type = when(currentItem.animeType){
                    "anime" -> " (anime)"
                    "manga" -> " (manga)"
                    else -> ""
                }

                tvRelatedTitle.text = currentItem.name + type
                tvRelatedTitle.setTextColor(titleTextColor)

                tvRelatedType.text = currentItem.relatedType
                tvRelatedType.setTextColor(titleTextColor)
            }

        }
    }
}