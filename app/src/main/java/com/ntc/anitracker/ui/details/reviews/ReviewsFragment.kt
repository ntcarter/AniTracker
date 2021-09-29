package com.ntc.anitracker.ui.details.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.reviews.Review
import com.ntc.anitracker.databinding.FragmentReviewsBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.ReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ReviewsFragment"

@AndroidEntryPoint
class ReviewsFragment : Fragment(R.layout.fragment_reviews), ReviewsAdapter.OnReviewTap {

    private val args by navArgs<ReviewsFragmentArgs>()

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    val viewModel: ReviewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentReviewsBinding.bind(view)

        val adapter = ReviewsAdapter(args.titleTextColor, args.bodyTextColor, this)

        binding.apply {
            rvReviews.adapter = adapter
            rvReviews.layoutManager = LinearLayoutManager(requireContext())
            rvReviews.setHasFixedSize(true)
            rvReviews.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            clReviews.setBackgroundColor(args.bgColor)
        }

        viewModel.getReviews(args.malId, args.isAnime).observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        viewModel.getReviews(args.malId, args.isAnime)
    }

    override fun onTapReview(review: Review) {
        showFullReviewDialog(review)
    }

    private fun showFullReviewDialog(review: Review){
        val dialog = MaterialAlertDialogBuilder(requireContext()).
                setMessage("Full Review: \n\n\n"
                + review.content
                )
            .setPositiveButton("close"){_,_ ->}
            .create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}