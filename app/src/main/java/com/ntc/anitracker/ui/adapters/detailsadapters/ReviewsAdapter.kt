package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.reviews.Review
import com.ntc.anitracker.databinding.ItemReviewBinding

private const val TAG = "ReviewsAdapter"

class ReviewsAdapter(
    var titleTextColor: Int,
    var bodyTextColor: Int,
    val listener: OnReviewTap
) : PagingDataAdapter<Review, ReviewsAdapter.ReviewViewHolder>(REVIEW_COMPARATOR) {

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Review) {
            binding.apply {

                root.setOnClickListener {
                    listener.onTapReview(currentItem)
                }

                Glide.with(itemView)
                    .load(currentItem.reviewer.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivUser)

                tvReviewDate.text = currentItem.date?.substring(0..9) ?: ""
                tvReviewSeen.text =
                    "Episodes Seen: ${currentItem.reviewer.episodes_seen.toString()}"
                tvReviewUsername.text = currentItem.reviewer.username

                tvReviewAnimation.text = "Animation: ${currentItem.reviewer.scores.animation}"
                tvReviewCharacter.text = "Character: ${currentItem.reviewer.scores.character}"
                tvReviewEnjoyment.text = "Enjoyment: ${currentItem.reviewer.scores.enjoyment}"
                tvReviewOverall.text = "Overall: ${currentItem.reviewer.scores.overall}"
                tvReviewSound.text = "Sound: ${currentItem.reviewer.scores.sound}"
                tvReviewStory.text = "Story: ${currentItem.reviewer.scores.story}"

                // color

                tvReviewUsername.setTextColor(titleTextColor)
                tvReviewScores.setTextColor(titleTextColor)
                tvReviewDate.setTextColor(titleTextColor)
                tvReviewTap.setTextColor(titleTextColor)

                tvReviewSeen.setTextColor(bodyTextColor)
                tvReviewOverall.setTextColor(bodyTextColor)
                tvReviewAnimation.setTextColor(bodyTextColor)
                tvReviewCharacter.setTextColor(bodyTextColor)
                tvReviewStory.setTextColor(bodyTextColor)
                tvReviewSound.setTextColor(bodyTextColor)
                tvReviewEnjoyment.setTextColor(bodyTextColor)
            }
        }
    }

    interface OnReviewTap{
        fun onTapReview(review: Review)
    }

    companion object {
        private val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
        }
    }
}