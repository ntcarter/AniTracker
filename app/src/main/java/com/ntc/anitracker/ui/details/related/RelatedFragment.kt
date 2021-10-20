package com.ntc.anitracker.ui.details.related

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.anime.CombinedRelated
import com.ntc.anitracker.databinding.FragmentRelatedBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.RelatedAdapter

class RelatedFragment : Fragment(R.layout.fragment_related), RelatedAdapter.OnRelatedClick {

    private val args by navArgs<RelatedFragmentArgs>()

    private var _binding: FragmentRelatedBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRelatedBinding.bind(view)

        val combinedRelatedList = combineRelated()

        binding.apply {
            clRelated.setBackgroundColor(args.bgColor)
            tvRelatedEmpty.setTextColor(args.titleTextColor)

            if(!combinedRelatedList.isNullOrEmpty()) {
                rvRelated.adapter =
                    RelatedAdapter(combinedRelatedList, args.titleTextColor, this@RelatedFragment)
                rvRelated.layoutManager = LinearLayoutManager(requireContext())
                rvRelated.setHasFixedSize(true)
                rvRelated.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                rvRelated.visibility = View.VISIBLE
                tvRelatedEmpty.visibility = View.GONE
            } else {
                rvRelated.visibility = View.GONE
                tvRelatedEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun combineRelated(): List<CombinedRelated> {
        val combinedResult: MutableList<CombinedRelated> = mutableListOf()
        val related = args.relatedMaterials

        if (!related.Sequel.isNullOrEmpty()) {
            for (sequel in related.Sequel) {
                combinedResult.add(
                    CombinedRelated(
                        sequel.mal_id,
                        sequel.name,
                        "Sequel",
                        sequel.type
                    )
                )
            }
        }

        if (!related.Side_story.isNullOrEmpty()) {
            for (sideStory in related.Side_story) {
                combinedResult.add(
                    CombinedRelated(
                        sideStory.mal_id,
                        sideStory.name,
                        "Side Story",
                        sideStory.type
                    )
                )
            }
        }

        if (!related.Adaptation.isNullOrEmpty()) {
            for (adaptation in related.Adaptation) {
                combinedResult.add(
                    CombinedRelated(
                        adaptation.mal_id,
                        adaptation.name,
                        "Adaptation",
                        adaptation.type
                    )
                )
            }
        }

        if (!related.Alternative_version.isNullOrEmpty()) {
            for (altVersion in related.Alternative_version) {
                combinedResult.add(
                    CombinedRelated(
                        altVersion.mal_id,
                        altVersion.name,
                        "Alternative Version",
                        altVersion.type
                    )
                )
            }
        }

        if (!related.Character.isNullOrEmpty()) {
            for (character in related.Character) {
                combinedResult.add(
                    CombinedRelated(
                        character.mal_id,
                        character.name,
                        "Character",
                        character.type
                    )
                )
            }
        }

        if (!related.Other.isNullOrEmpty()) {
            for (other in related.Other) {
                combinedResult.add(
                    CombinedRelated(
                        other.mal_id,
                        other.name,
                        "Other",
                        other.type
                    )
                )
            }
        }

        return combinedResult
    }

    override fun onRelatedAnimeClick(malId: Int) {
        val action = RelatedFragmentDirections.actionRelatedFragmentToAnimeDetailsFragment(malId)
        findNavController().navigate(action)
    }

    override fun onRelatedMangaClick(malId: Int) {
        val action = RelatedFragmentDirections.actionRelatedFragmentToMangaDetailsFragment(malId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}