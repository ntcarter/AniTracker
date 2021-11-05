package com.ntc.anitracker.ui.details.animedetails

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
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
import com.ntc.anitracker.ui.details.dialog.PictureDetailsDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt

private const val TAG = "AnimeDetailsFragment"

@AndroidEntryPoint
class AnimeDetailsFragment : Fragment(R.layout.fragment_anime_details),
    PictureDetailsDialogFragment.OnDialogState {

    private val args by navArgs<AnimeDetailsFragmentArgs>()

    private val viewModel: AnimeDetailsViewModel by viewModels()

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!

    // Holds the state of an active image dialog. If true there's an active dialog currently showing
    private var dialogState = false
    private var apiState = false

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
            // occurs on process death like screen rotations
            loadImage(viewModel.anime.value!!)
        }

        // sometimes the UI doesn't update if the network lags wait 5 seconds and if the default values are still active, update
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
                var bgColor = palette.darkMutedSwatch ?: Palette.Swatch(ContextCompat.getColor(requireContext(), R.color.background_black), 1)

                val contrast = calcContrast(
                    bgColor,
                    palette.lightMutedSwatch,
                    palette.mutedSwatch,
                    palette.vibrantSwatch,
                    palette.darkVibrantSwatch,
                    palette.darkMutedSwatch,
                    palette.dominantSwatch,
                    palette.lightVibrantSwatch,
                )

                // sometimes button background color is too similar to its text color.
                var btnColor =
                    // Check for the contrast if its too low pick a different color
                    if (abs(contrast["lightMutedSwatch"]!! - contrast["dominantSwatch"]!!) < 100 || contrast["dominantSwatch"] == 0.0) {
                        palette.darkVibrantSwatch ?: palette.mutedSwatch ?: palette.vibrantSwatch
                    } else {
                        palette.dominantSwatch
                    }

                // sometimes the button color will be the same as the background color
                if(contrast["darkMutedSwatch"] == contrast["dominantSwatch"] && contrast["dominantSwatch"] != 0.0) {
                    btnColor = palette.vibrantSwatch ?: palette.mutedSwatch
                }

                val bdyTextColor =
                    // Check for the contrast if its too low pick a different color
                    if (contrast["lightMutedSwatch"]!! < 150) {
                        when {
                            contrast["mutedSwatch"]!! > 150 -> {
                                palette.mutedSwatch
                            }
                            contrast["vibrantSwatch"]!! > 150 -> {
                                palette.vibrantSwatch
                            }
                            contrast["darkVibrantSwatch"]!! > 150 -> {
                                palette.darkVibrantSwatch
                            }
                            else -> {
                                palette.dominantSwatch ?: palette.darkMutedSwatch ?: palette.lightMutedSwatch
                            }
                        }

                    } else {
                        palette.lightMutedSwatch
                    }



                updateColors(
                    bdyTextColor,
                    bgColor,
                    // sometimes the palette will not return a vibrantSwatch
                    palette.vibrantSwatch ?: palette.mutedSwatch,
                    btnColor,
                    animeInfo
                )
            }
        }
    }

    private fun calcContrast(
        bgColor: Palette.Swatch?,
        lightMutedSwatch: Palette.Swatch?,
        mutedSwatch: Palette.Swatch?,
        vibrantSwatch: Palette.Swatch?,
        darkVibrantSwatch: Palette.Swatch?,
        darkMutedSwatch: Palette.Swatch?,
        dominantSwatch: Palette.Swatch?,
        lightVibrantSwatch: Palette.Swatch?
    ): HashMap<String, Double> {
        val result = HashMap<String, Double>()

        if (bgColor != null) {
            result["lightMutedSwatch"] = if (lightMutedSwatch != null) {
                calculate(lightMutedSwatch, bgColor)
            } else {
                0.0
            }
            result["mutedSwatch"] = if (mutedSwatch != null) {
                calculate(mutedSwatch, bgColor)
            } else {
                0.0
            }
            result["vibrantSwatch"] = if (vibrantSwatch != null) {
                calculate(vibrantSwatch, bgColor)
            } else {
                0.0
            }
            result["darkVibrantSwatch"] = if (darkVibrantSwatch != null) {
                calculate(darkVibrantSwatch, bgColor)
            } else {
                0.0
            }
            result["darkMutedSwatch"] = if (darkMutedSwatch != null) {
                calculate(darkMutedSwatch, bgColor)
            } else {
                0.0
            }
            result["dominantSwatch"] = if (dominantSwatch != null) {
                calculate(dominantSwatch, bgColor)
            } else {
                0.0
            }
            result["lightVibrantSwatch"] = if (lightVibrantSwatch != null) {
                calculate(lightVibrantSwatch, bgColor)
            } else {
                0.0
            }
        }
        return result
    }

    private fun calculate(swatch: Palette.Swatch, bgColor: Palette.Swatch): Double {
        val rMean = swatch.rgb.red.toLong() + bgColor.rgb.red.toLong()
        val r = swatch.rgb.red.toLong() - bgColor.rgb.red.toLong()
        val g = swatch.rgb.green.toLong() - bgColor.rgb.green.toLong()
        val b = swatch.rgb.blue.toLong() - bgColor.rgb.blue.toLong()
        return sqrt((((512 + rMean) * r * r shr 8) + 4 * g * g + ((767 - rMean) * b * b shr 8)).toDouble())
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

                // buttons
                btnCharactersAndStaff.setTextColor(bodyTextColor.rgb)
                btnEpisodeinfo.setTextColor(bodyTextColor.rgb)
                btnReviews.setTextColor(bodyTextColor.rgb)
                btnRecommendations.setTextColor(bodyTextColor.rgb)
                btnRelated.setTextColor(bodyTextColor.rgb)
            }

            if (backgroundColor != null) {
                bgColor = backgroundColor.rgb
                svDetails.setBackgroundColor(backgroundColor.rgb)
            }else {
                bgColor = R.color.background_black
                svDetails.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_black))
            }

            if (titleTextColor != null) {
                textTitleColor = titleTextColor.rgb
                tvTextEpisodes.setTextColor(titleTextColor.rgb)
                tvTextScore.setTextColor(titleTextColor.rgb)
                tvUsersText.setTextColor(titleTextColor.rgb)
                tvTextEpisodes.setTextColor(titleTextColor.rgb)
                tvPopularityText.setTextColor(titleTextColor.rgb)
                tvStartedAiringText.setTextColor(titleTextColor.rgb)
                tvFinishedAiringText.setTextColor(titleTextColor.rgb)
                tvSynopsisText.setTextColor(titleTextColor.rgb)
                tvGenresText.setTextColor(titleTextColor.rgb)
                tvOpeningsText.setTextColor(titleTextColor.rgb)
                tvEndingsText.setTextColor(titleTextColor.rgb)
                tvStudiosText.setTextColor(titleTextColor.rgb)
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
            } else if (animeInfo.airing) {
                "Currently Airing"
            } else {
                ""
            }
            tvTextScore.text = "Score:"
            tvUsersText.text = "Users:"
            tvTextEpisodes.text = "Episodes:"
            tvPopularityText.text = "Popularity"
            tvStartedAiringText.text = "Started Airing:"
            tvFinishedAiringText.text = "Finished Airing:"
            tvSynopsisText.text = "Synopsis:"
            tvGenresText.text = "Genres:"
            tvOpeningsText.text = "Opening Themes:"
            tvEndingsText.text = "Ending Themes:"
            tvStudiosText.text = "Studios:"
            btnCharactersAndStaff.text = "Characters And Staff Information"
            btnRecommendations.text = "Recommendations"
            btnReviews.text = "Reviews"
            btnEpisodeinfo.text = "Episode Information"
            btnRelated.text = "Related"

            tvEpisodeCount.text = animeInfo.episodes.toString()
            tvPopularity.text = animeInfo.popularity.toString()
            tvAnimeNameEn.text = animeInfo.title_english ?: animeInfo.title
            tvAnimeNameEn.setOnClickListener(listener)
            tvAnimeNameJp.text = animeInfo.title_japanese
            tvAnimeNameJp.setOnClickListener(listener)
            tvSynopsis.text = animeInfo.synopsis

            tvStudios.text = viewModel.getStudioList(animeInfo.studios)
            tvOpeningThemes.text = viewModel.getOpeningsList(animeInfo.opening_themes)
            tvEndingThemes.text = viewModel.getOpeningsList(animeInfo.ending_themes)
            tvGenres.text = viewModel.getGenreList(animeInfo.genres)

            // buttons
            btnCharactersAndStaff.setOnClickListener {
                val action =
                    AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToCharactersAndStaffFragment(
                        animeInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        btnColor,
                        bgColor,
                        "anime"
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
                        bgColor,
                        true
                    )
                findNavController().navigate(action)
            }

            ivCover.setOnClickListener {
                // set up the observer
                viewModel.images.observe(viewLifecycleOwner, {
                    // create the dialog with the image data
                    if (it != null && !dialogState) {
                        val dialog =
                            PictureDetailsDialogFragment(it.pictures, this@AnimeDetailsFragment)
                        dialog.show(parentFragmentManager, "dialog")
                        dialogState =
                            true // lock the observer out of making anymore dialogs while one is open
                    }
                })

                // make the api call
                if (!apiState) {
                    apiState = true
                    viewModel.getAnimeImageData(args.malId)
                }
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

    override fun onDialogDismiss() {
        dialogState = false // unlock the ability for the fragment to create a new dialog
        apiState = false // Unlock the ability to make another image api call
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}