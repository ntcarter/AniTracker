package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.person.PublishedManga
import com.ntc.anitracker.databinding.ItemImageTwoTextBinding

class PublishedMangaAdapter(
    private val publishedMangaList: List<PublishedManga>,
    val textColor: Int,
    val listener: VoiceRolesAdapter.OnPersonDetailsClick
) : RecyclerView.Adapter<PublishedMangaAdapter.PublishedMangaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublishedMangaViewHolder {
        val binding =
            ItemImageTwoTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublishedMangaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PublishedMangaViewHolder, position: Int) {
        val currentItem = publishedMangaList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = publishedMangaList.size

    inner class PublishedMangaViewHolder(private val binding: ItemImageTwoTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: PublishedManga) {

            binding.apply {
                Glide.with(itemView)
                    .load(currentItem.manga.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivVoiceActor)

                tvVAName.text = currentItem.manga.name
                tvVAName.setTextColor(textColor)
                tvVALang.text = currentItem.position
                tvVALang.setTextColor(textColor)

                //TODO navigate to manga
            }
        }
    }
}