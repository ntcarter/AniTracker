package com.ntc.anitracker.ui.details.animedetails

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.anime.Anime
import com.ntc.anitracker.databinding.FragmentAnimeDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "AnimeDetailsFragment"

@AndroidEntryPoint
class AnimeDetailsFragment : Fragment(R.layout.fragment_anime_details) {

    private val args by navArgs<AnimeDetailsFragmentArgs>()

    private val viewModel: AnimeDetailsViewModel by viewModels()

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!

    private var textBodyColor: Int = 0
    private var bgColor: Int = 0
    private var textTitleColor: Int = 0
    private var btnColor: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAnimeDetailsBinding.bind(view)

        viewModel.anime.observe(viewLifecycleOwner, { anime ->
            anime?.let { loadImage(anime) }
        })

        if (viewModel.anime.value == null) {
            // no anime data need to load new data from API
            viewModel.getAnimeData(args.malId)
        } else {
            // anime already has a value use that to bind instead of an api call
            // happens for things like screen rotations
            loadImage(viewModel.anime.value!!)
        }

        // sometimes the UI doesn't update wait 5 seconds and if the default values are still active, update
        lifecycleScope.launch {
            delay(3000)
            if (_binding != null && binding.tvAnimeNameEn.text == "Loading") {
                if (viewModel.anime.value != null) {
                    loadImage(viewModel.anime.value!!)
                } else {
                    viewModel.getAnimeData(args.malId)
                }
            }
        }
    }

    /**
     * Loads the image into the imageView. This needs to be done before the rest of the UI since
     * the UI text colors are calculated from the image
     */
    private fun loadImage(animeInfo: Anime) {
        binding.apply {
            Glide.with(requireActivity())
                .asBitmap()
                .load(animeInfo.image_url)
                .error(R.drawable.ic_error)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        ivCover.setImageBitmap(resource)
                        createPaletteAsync(resource, animeInfo)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }

    /**
     * Takes the cover image as a bitmap and calculates a color palette
     */
    private fun createPaletteAsync(bitmap: Bitmap, animeInfo: Anime) {
        Palette.from(bitmap).generate() { palette ->
            if (palette != null) {
                updateColors(
                    palette.lightMutedSwatch,
                    palette.darkMutedSwatch,
                    // sometimes the palette will not return a vibrantSwatch
                    palette.vibrantSwatch ?: palette.mutedSwatch,
                    palette.darkVibrantSwatch,
                    animeInfo
                )
            }
        }
    }

    private fun updateColors(
        bodyTextColor: Palette.Swatch?,
        backgroundColor: Palette.Swatch?,
        titleTextColor: Palette.Swatch?,
        buttonColor: Palette.Swatch?,
        animeInfo: Anime
    ) {

        binding.apply {

            if (bodyTextColor != null) {
                textBodyColor = bodyTextColor.rgb
                tvAnimeScore.setTextColor(bodyTextColor.rgb)
                tvUsersAmount.setTextColor(bodyTextColor.rgb)
                tvAiringStartTime.setTextColor(bodyTextColor.rgb)
                tvAiringEnd.setTextColor(bodyTextColor.rgb)
                tvEpisodeCount.setTextColor(bodyTextColor.rgb)
                tvPopularity.setTextColor(bodyTextColor.rgb)
                tvAnimeNameEn.setTextColor(bodyTextColor.rgb)
                tvAnimeNameJp.setTextColor(bodyTextColor.rgb)
                tvSynopsis.setTextColor(bodyTextColor.rgb)
                tvStudios.setTextColor(bodyTextColor.rgb)
                tvOpeningThemes.setTextColor(bodyTextColor.rgb)
                tvEndingThemes.setTextColor(bodyTextColor.rgb)
                tvGenres.setTextColor(bodyTextColor.rgb)
                tvMyScore.setTextColor(bodyTextColor.rgb)
            }

            if (backgroundColor != null) {
                bgColor = backgroundColor.rgb
                svDetails.setBackgroundColor(backgroundColor.rgb)
            }

            if (titleTextColor != null) {
                textTitleColor = titleTextColor.rgb
                tvTextEpisodes.setTextColor(titleTextColor.rgb)
                tvTextScore.setTextColor(titleTextColor.rgb)
                tvUsersText.setTextColor(titleTextColor.rgb)
                tvMyScoreText.setTextColor(titleTextColor.rgb)
                tvTextEpisodes.setTextColor(titleTextColor.rgb)
                tvPopularityText.setTextColor(titleTextColor.rgb)
                tvStartedAiringText.setTextColor(titleTextColor.rgb)
                tvFinishedAiringText.setTextColor(titleTextColor.rgb)
                tvSynopsisText.setTextColor(titleTextColor.rgb)
                tvGenresText.setTextColor(titleTextColor.rgb)
                tvOpeningsText.setTextColor(titleTextColor.rgb)
                tvEndingsText.setTextColor(titleTextColor.rgb)
                tvStudiosText.setTextColor(titleTextColor.rgb)

                // buttons
                btnCharactersAndStaff.setTextColor(titleTextColor.rgb)
                btnEpisodeinfo.setTextColor(titleTextColor.rgb)
                btnReviews.setTextColor(titleTextColor.rgb)
                btnRecommendations.setTextColor(titleTextColor.rgb)
                btnRelated.setTextColor(titleTextColor.rgb)
            }

            if (buttonColor != null) {
                btnColor = buttonColor.rgb
                btnCharactersAndStaff.setBackgroundColor(buttonColor.rgb)
                btnEpisodeinfo.setBackgroundColor(buttonColor.rgb)
                btnReviews.setBackgroundColor(buttonColor.rgb)
                btnRecommendations.setBackgroundColor(buttonColor.rgb)
                btnRelated.setBackgroundColor(buttonColor.rgb)
            }

            bindUI(animeInfo)
        }
    }

    private fun bindUI(animeInfo: Anime) {
        val listener = View.OnClickListener {
            showFullTitleDialog(animeInfo)
        }

        binding.apply {

            tvAnimeScore.text = animeInfo.score.toString()
            tvUsersAmount.text = animeInfo.scored_by.toString()
            tvAiringStartTime.text = if (animeInfo.aired.from != null) {
                animeInfo.aired.from.subSequence(0..9)
            } else {
                ""
            }
            tvAiringEnd.text = if (animeInfo.aired.to != null) {
                animeInfo.aired.to.subSequence(0..9)
            } else {
                ""
            }
            tvTextScore.text = "Score:"
            tvUsersText.text = "Users:"
            tvMyScoreText.text = "My Score:"
            tvTextEpisodes.text = "Episodes:"
            tvPopularityText.text = "Popularity"
            tvStartedAiringText.text = "Started Airing:"
            tvFinishedAiringText.text = "Finished Airing:"
            tvSynopsisText.text = "Synopsis:"
            tvGenresText.text = "Genres:"
            tvOpeningsText.text = "Opening Themes:"
            tvEndingsText.text = "Ending Themes:"
            tvStudiosText.text = "Studios:"
            tvMyScore.text = "--"
            btnCharactersAndStaff.text = "Characters And Staff Information"
            btnRecommendations.text = "Recommendations"
            btnReviews.text = "Reviews"
            btnEpisodeinfo.text = "Episode Information"

            tvEpisodeCount.text = animeInfo.episodes.toString()
            tvPopularity.text = animeInfo.popularity.toString()
            tvAnimeNameEn.text = animeInfo.title_english ?: animeInfo.title
            tvAnimeNameEn.setOnClickListener(listener)
            tvAnimeNameJp.text = animeInfo.title_japanese
            tvAnimeNameJp.setOnClickListener(listener)
            tvSynopsis.text = animeInfo.synopsis

            var studios = ""
            val studioList = animeInfo.studios
            for (studio in studioList) {
                if (studio != studioList[studioList.size - 1]) {
                    studios += "${studio.name}, "
                } else { // last element
                    studios += studio.name
                }
            }
            tvStudios.text = studios

            var openings = ""
            val openingsList = animeInfo.opening_themes

            Log.d(TAG, "bindUI: ${animeInfo.opening_themes}")
            for (opening in openingsList) {
                if (opening != openingsList[openingsList.size - 1]) {
                    openings += "$opening, \n\n"
                } else { // last element
                    openings += opening
                }
            }
            tvOpeningThemes.text = openings

            var endings = ""
            val endingsList = animeInfo.ending_themes
            for (ending in endingsList) {
                if (ending != endingsList[endingsList.size - 1]) {
                    endings += "$ending, \n\n"
                } else { // last element
                    endings += ending
                }
            }
            tvEndingThemes.text = endings

            var genres = ""
            val genresList = animeInfo.genres
            for (genre in genresList) {
                if (genre.name != genresList[genresList.size - 1].name) {
                    genres += "${genre.name}, "
                } else { // last element
                    genres += genre.name
                }
            }
            tvGenres.text = genres

            // buttons
            btnCharactersAndStaff.setOnClickListener {
                val action =
                    AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToCharactersAndStaffFragment(
                        animeInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        btnColor,
                        bgColor
                    )
                findNavController().navigate(action)
            }

            btnRecommendations.setOnClickListener {
                val action =
                    AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToRecomendationsFragment(
                        animeInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        bgColor,
                        true,
                        animeInfo.title
                    )
                findNavController().navigate(action)
            }

            btnEpisodeinfo.setOnClickListener {
                val action =
                    AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToEpisodesFragment(
                        animeInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        bgColor
                    )
                findNavController().navigate(action)
            }

            btnReviews.setOnClickListener {
                val action =
                    AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToFragmentReviews(
                        animeInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        true,
                        bgColor
                    )
                findNavController().navigate(action)
            }

            btnRelated.setOnClickListener {
                val action =
                    AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToRelatedFragment(
                        animeInfo.related,
                        textTitleColor,
                        textBodyColor,
                        bgColor
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun showFullTitleDialog(animeInfo: Anime) {
        // TODO ?? GET BETTER COLORS? Will need to set them programmatically not through style
        val englishTitle = animeInfo.title_english ?: animeInfo.title
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage(
                "English Title: \n\n"
                        + "$englishTitle \n\n\n\n"
                        + "Japanese Title: \n\n"
                        + "${animeInfo.title_japanese} \n\n"
            )
            .setPositiveButton("Close") { _, _ -> }
            .create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}