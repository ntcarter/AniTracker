package com.ntc.anitracker.ui.details.recommendation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.recommendations.Recommendation
import com.ntc.anitracker.databinding.FragmentRecommendationsBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.RecommendationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationsFragment : Fragment(R.layout.fragment_recommendations),
    RecommendationAdapter.OnRecClick {

    private val args by navArgs<RecommendationsFragmentArgs>()

    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRecommendationsBinding.bind(view)

        viewModel.recDetails.observe(viewLifecycleOwner, { recList ->
            recList?.let {
                bindUI(recList.recommendations)
            }
        })

        binding.apply {
            clBg.setBackgroundColor(args.bgColor)

            if (args.isAnime) {
                tvRecHeader.text = "Anime Similar To: ${args.title}"
            } else {
                tvRecHeader.text = "Manga Similar To: ${args.title}"
            }
            tvRecHeader.setTextColor(args.titleTextColor)
        }

        viewModel.getRecommendations(args.malId, args.isAnime)
    }

    private fun bindUI(recList: List<Recommendation>) {
        binding.apply {

            val adapter = RecommendationAdapter(
                recList,
                args.titleTextColor,
                args.bodyTextColor,
                this@RecommendationsFragment,
                args.isAnime
            )

            rvRec.adapter = adapter
            rvRec.layoutManager = LinearLayoutManager(requireContext())
            rvRec.setHasFixedSize(true)
            rvRec.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onRecAnimeClick(malId: Int) {
        val action =
            RecommendationsFragmentDirections.actionRecomendationsFragmentToAnimeDetailsFragment(
                malId
            )
        findNavController().navigate(action)
    }

    override fun onRecMangaClick(malId: Int) {
        val action =
            RecommendationsFragmentDirections.actionRecomendationsFragmentToMangaDetailsFragment(
                malId
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}