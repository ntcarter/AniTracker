package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.person.AnimeStaffPosition
import com.ntc.anitracker.databinding.ItemAnimeStaffPositionsBinding

class AnimeStaffPositionsAdapter(
    private val staffPositionList: List<AnimeStaffPosition>,
    val textColor: Int,
    val listener: VoiceRolesAdapter.OnPersonDetailsClick
) : RecyclerView.Adapter<AnimeStaffPositionsAdapter.AnimeStaffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeStaffViewHolder {
        val binding =
            ItemAnimeStaffPositionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeStaffViewHolder, position: Int) {
        val currentItem = staffPositionList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = staffPositionList.size

    inner class AnimeStaffViewHolder(private val binding: ItemAnimeStaffPositionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: AnimeStaffPosition) {

            binding.apply {

                root.setOnClickListener {
                    listener.onAnimeClick(currentItem.anime.mal_id)
                }

                Glide.with(itemView)
                    .load(currentItem.anime.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivStaffPosition)

                tvAnimeStaffName.text = currentItem.anime.name
                tvAnimeStaffName.setTextColor(textColor)
                tvAnimeStaffRole.text = currentItem.position
                tvAnimeStaffRole.setTextColor(textColor)
            }
        }
    }
}