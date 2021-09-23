package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.charactersandstaff.VoiceActor
import com.ntc.anitracker.databinding.ItemImageTwoTextBinding

class VoiceActorAdapter(
    private val voiceActorList: List<VoiceActor>,
    val textColor: Int
) : RecyclerView.Adapter<VoiceActorAdapter.VoiceActorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceActorViewHolder {
        val binding =
            ItemImageTwoTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoiceActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoiceActorViewHolder, position: Int) {
        val currentItem = voiceActorList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return voiceActorList.size
    }

    inner class VoiceActorViewHolder(private val binding: ItemImageTwoTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(voiceActor: VoiceActor) {
            binding.apply {
                Glide.with(itemView)
                    .load(voiceActor.image_url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivVoiceActor)
                tvVAName.text = voiceActor.name
                tvVAName.setTextColor(textColor)
                tvVALang.text = voiceActor.language
                tvVALang.setTextColor(textColor)
            }
        }
    }
}