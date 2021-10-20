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
    val listener: OnReviewTap,
    val isAnime: Boolean
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

                if(isAnime){
                    // anime review
                    tvReviewSeen.text =
                        "Episodes Read: ${currentItem.reviewer.episodes_seen.toString()}"
                    tvReviewUsername.text = currentItem.reviewer.username

                    tvReviewAnimation.text = "Animation:"
                    tvAnimationScore.text = "${currentItem.reviewer.scores.animation}"

                    tvReviewCharacter.text = "Character:"
                    tvCharacterScore.text = "${currentItem.reviewer.scores.character}"

                    tvReviewEnjoyment.text = "Enjoyment:"
                    tvEnjoymentScore.text = "${currentItem.reviewer.scores.enjoyment}"

                    tvReviewOverall.text = "Overall:"
                    tvOverallScore.text = "${currentItem.reviewer.scores.overall}"

                    tvReviewSound.text = "Sound:"
                    tvSoundScore.text = "${currentItem.reviewer.scores.sound}"

                    tvReviewStory.text = "Story:"
                    tvStoryScore.text = "${currentItem.reviewer.scores.story}"
                } else {
                    // manga review
                    tvReviewSeen.text =
                        "Chapters Seen: ${currentItem.reviewer.chapters_read.toString()}"
                    tvReviewUsername.text = currentItem.reviewer.username

                    tvReviewAnimation.text = "Art:"
                    tvAnimationScore.text = "${currentItem.reviewer.scores.art}"

                    tvReviewCharacter.text = "Character:"
                    tvCharacterScore.text = "${currentItem.reviewer.scores.character}"

                    tvReviewEnjoyment.text = ""
                    tvEnjoymentScore.text = ""

                    tvReviewOverall.text = "Overall:"
                    tvOverallScore.text = "${currentItem.reviewer.scores.overall}"

                    tvReviewSound.text = "Enjoyment:"
                    tvSoundScore.text = "${currentItem.reviewer.scores.enjoyment}"

                    tvReviewStory.text = "Story:"
                    tvStoryScore.text = "${currentItem.reviewer.scores.story}"
                }

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

                tvOverallScore.setTextColor(bodyTextColor)
                tvAnimationScore.setTextColor(bodyTextColor)
                tvCharacterScore.setTextColor(bodyTextColor)
                tvStoryScore.setTextColor(bodyTextColor)
                tvSoundScore.setTextColor(bodyTextColor)
                tvEnjoymentScore.setTextColor(bodyTextColor)
            }
        }
    }

    interface OnReviewTap {
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