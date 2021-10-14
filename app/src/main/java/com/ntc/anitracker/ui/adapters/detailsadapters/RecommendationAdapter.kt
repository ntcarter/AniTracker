package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.recommendations.Recommendation
import com.ntc.anitracker.databinding.ItemRecommendationBinding

class RecommendationAdapter(
    private val recList: List<Recommendation>,
    val titleTextColor: Int,
    val bodyTextColor: Int,
    val listener: OnRecClick,
    val isAnime: Boolean
) : RecyclerView.Adapter<RecommendationAdapter.RecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val binding =
            ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        val currentItem = recList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = recList.size

    inner class RecViewHolder(private val binding: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rec: Recommendation) {
            binding.apply {
                Glide.with(itemView)
                    .load(rec.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivRec)

                if(isAnime){
                    ivRec.setOnClickListener {
                        listener.onRecAnimeClick(rec.mal_id)
                    }
                }else {
                    ivRec.setOnClickListener {
                        listener.onRecMangaClick(rec.mal_id)
                    }
                }

                tvRecName.text = rec.title
                tvRecName.setTextColor(titleTextColor)
                tvRecInfo.text = "recommended by ${rec.recommendation_count} people"
                tvRecInfo.setTextColor(bodyTextColor)
            }
        }
    }

    interface OnRecClick {
        fun onRecAnimeClick(malId: Int)
        fun onRecMangaClick(malId: Int)
    }
}