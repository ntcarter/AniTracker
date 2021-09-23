package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.characterdetails.OGraphy
import com.ntc.anitracker.databinding.ItemImageTwoTextBinding

class oGraphyAdapter(
    private val oGraphyList: List<OGraphy>,
    val textColor: Int,
    val listener: OnOgraphyClick,
    val isAnime: Boolean // true if anime false if manga
) : RecyclerView.Adapter<oGraphyAdapter.OGraphyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OGraphyViewHolder {
        val binding =
            ItemImageTwoTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OGraphyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OGraphyViewHolder, position: Int) {
        val currentItem = oGraphyList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = oGraphyList.size

    inner class OGraphyViewHolder(private val binding: ItemImageTwoTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(item: OGraphy) {
            binding.root.setOnClickListener {
                if(isAnime){
                    listener.onAnimeographyCLick(item.mal_id)
                } else {
                    listener.onMangaographyCLick(item.mal_id)
                }
            }

            binding.apply {
                Glide.with(itemView)
                    .load(item.image_url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivVoiceActor)
                tvVAName.text = item.name
                tvVAName.setTextColor(textColor)
                tvVALang.text = item.role
                tvVALang.setTextColor(textColor)
            }
        }

    }

    interface OnOgraphyClick{
        fun onAnimeographyCLick(malId: Int)
        fun onMangaographyCLick(malId: Int)
    }
}