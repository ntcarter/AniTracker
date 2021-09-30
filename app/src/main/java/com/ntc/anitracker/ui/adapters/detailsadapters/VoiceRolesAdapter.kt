package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.person.VoiceActingRole
import com.ntc.anitracker.databinding.ItemVoiceActorBinding

class VoiceRolesAdapter(
    private val voiceRolesList: List<VoiceActingRole>,
    val textColor: Int,
    val listener: OnPersonDetailsClick
) : RecyclerView.Adapter<VoiceRolesAdapter.VoiceRolesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceRolesViewHolder {
        val binding =
            ItemVoiceActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoiceRolesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoiceRolesViewHolder, position: Int) {
        val currentItem = voiceRolesList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = voiceRolesList.size

    interface OnPersonDetailsClick {
        fun onAnimeClick(malId: Int)
        fun onCharacterClick(malId: Int)
    }

    inner class VoiceRolesViewHolder(private val binding: ItemVoiceActorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: VoiceActingRole) {
            binding.apply {
                Glide.with(itemView)
                    .load(currentItem.character.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivVAChar)

                Glide.with(itemView)
                    .load(currentItem.anime.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivVAAnime)

                tvVACharRole.text = currentItem.role
                tvVACharRole.setTextColor(textColor)

                tvVACharName.text = currentItem.character.name
                tvVACharName.setTextColor(textColor)

                tvVAAnimeName.text = currentItem.anime.name
                tvVAAnimeName.setTextColor(textColor)

                viewAnimeClick.setOnClickListener {
                    listener.onAnimeClick(currentItem.anime.mal_id)
                }

                viewCharacterClick.setOnClickListener {
                    listener.onCharacterClick(currentItem.character.mal_id)
                }
            }
        }
    }
}