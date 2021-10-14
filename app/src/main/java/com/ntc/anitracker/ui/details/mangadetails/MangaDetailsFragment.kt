package com.ntc.anitracker.ui.details.mangadetails

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
import com.ntc.anitracker.api.models.manga.Manga
import com.ntc.anitracker.databinding.FragmentMangaDetailsBinding
import com.ntc.anitracker.ui.details.dialog.PictureDetailsDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "AnimeDetailsFragment"

@AndroidEntryPoint
class MangaDetailsFragment : Fragment(R.layout.fragment_manga_details), PictureDetailsDialogFragment.OnDialogState {

    private val args by navArgs<MangaDetailsFragmentArgs>()

    private val viewModel: MangaDetailsViewModel by viewModels()

    private var _binding: FragmentMangaDetailsBinding? = null
    private val binding get() = _binding!!

    // Holds the state of an active image dialog. If true there's an active dialog currently showing
    private var dialogState = false
    // Holds the state of the Image api call so multiple calls cannot be made before a response is received
    private var apiState = false

    private var textBodyColor: Int = 0
    private var bgColor: Int = 0
    private var textTitleColor: Int = 0
    private var btnColor: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMangaDetailsBinding.bind(view)

        viewModel.manga.observe(viewLifecycleOwner, { manga ->
            manga?.let { loadImage(manga) }
        })

        if (viewModel.manga.value == null) {
            // no anime data need to load new data from API
            viewModel.getMangaData(args.malId)
        } else {
            // anime already has a value use that to bind instead of an api call
            // happens for things like screen rotations
            loadImage(viewModel.manga.value!!)
        }

        // sometimes the UI doesn't update wait 5 seconds and if the default values are still active, update
        lifecycleScope.launch {
            delay(3000)
            if (_binding != null && binding.tvMangaNameEn.text == "Loading") {
                if (viewModel.manga.value != null) {
                    loadImage(viewModel.manga.value!!)
                } else {
                    viewModel.getMangaData(args.malId)
                }
            }
        }
    }

    /**
     * Loads the image into the imageView. This needs to be done before the rest of the UI since
     * the UI text colors are calculated from the image
     */
    private fun loadImage(mangaInfo: Manga) {
        binding.apply {
            Glide.with(requireActivity())
                .asBitmap()
                .load(mangaInfo.image_url)
                .error(R.drawable.ic_error)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        ivMangaCover.setImageBitmap(resource)
                        createPaletteAsync(resource, mangaInfo)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }

    /**
     * Takes the cover image as a bitmap and calculates a color palette
     */
    private fun createPaletteAsync(bitmap: Bitmap, mangaInfo: Manga) {
        Palette.from(bitmap).generate() { palette ->
            if (palette != null) {
                updateColors(
                    palette.lightMutedSwatch ?: palette.mutedSwatch,
                    palette.darkMutedSwatch,
                    // sometimes the palette will not return a vibrantSwatch
                    palette.vibrantSwatch ?: palette.mutedSwatch,
                    palette.darkVibrantSwatch,
                    mangaInfo
                )
            }
        }
    }

    private fun updateColors(
        bodyTextColor: Palette.Swatch?,
        backgroundColor: Palette.Swatch?,
        titleTextColor: Palette.Swatch?,
        buttonColor: Palette.Swatch?,
        mangaInfo: Manga
    ) {

        binding.apply {

            if (bodyTextColor != null) {
                textBodyColor = bodyTextColor.rgb
                tvMangaAnimeScore.setTextColor(bodyTextColor.rgb)
                tvMangaUsersAmount.setTextColor(bodyTextColor.rgb)
                tvMangaPublishStartTime.setTextColor(bodyTextColor.rgb)
                tvMangaPublishingEnd.setTextColor(bodyTextColor.rgb)
                tvMangaChapterCount.setTextColor(bodyTextColor.rgb)
                tvMangaPopularity.setTextColor(bodyTextColor.rgb)
                tvMangaNameEn.setTextColor(bodyTextColor.rgb)
                tvMangaNameJp.setTextColor(bodyTextColor.rgb)
                tvMangaSynopsis.setTextColor(bodyTextColor.rgb)
                tvMangaSerializations.setTextColor(bodyTextColor.rgb)
                tvMangaGenres.setTextColor(bodyTextColor.rgb)
                tvMangaMyScore.setTextColor(bodyTextColor.rgb)
                tvMangaVolumes.setTextColor(bodyTextColor.rgb)
            } else {
                Log.d(TAG, "updateColors: NULLLLLLLLLLLLLLLLLLLLLLL")
            }

            if (backgroundColor != null) {
                bgColor = backgroundColor.rgb
                svMangaDetails.setBackgroundColor(backgroundColor.rgb)
            }

            if (titleTextColor != null) {
                textTitleColor = titleTextColor.rgb
                tvMangaStartedPublishingText.setTextColor(titleTextColor.rgb)
                tvMangaFinishedPublishingText.setTextColor(titleTextColor.rgb)
                tvSerializationsText.setTextColor(titleTextColor.rgb)
                tvMangaTextChapters.setTextColor(titleTextColor.rgb)
                tvMangaTextScore.setTextColor(titleTextColor.rgb)
                tvMangaUsersText.setTextColor(titleTextColor.rgb)
                tvMangaMyScoreText.setTextColor(titleTextColor.rgb)
                tvMangaPopularityText.setTextColor(titleTextColor.rgb)
                tvMangaStartedPublishingText.setTextColor(titleTextColor.rgb)
                tvMangaFinishedPublishingText.setTextColor(titleTextColor.rgb)
                tvMangaSynopsisText.setTextColor(titleTextColor.rgb)
                tvMangaGenresText.setTextColor(titleTextColor.rgb)
                tvSerializationsText.setTextColor(titleTextColor.rgb)
                tvMangaVolumesText.setTextColor(titleTextColor.rgb)

                // buttons
                btnMangaCharactersAndStaff.setTextColor(titleTextColor.rgb)
                btnMangaReviews.setTextColor(titleTextColor.rgb)
                btnMangaRecommendations.setTextColor(titleTextColor.rgb)
                btnMangaRelated.setTextColor(titleTextColor.rgb)
            }

            if (buttonColor != null) {
                btnColor = buttonColor.rgb
                btnMangaCharactersAndStaff.setBackgroundColor(buttonColor.rgb)
                btnMangaReviews.setBackgroundColor(buttonColor.rgb)
                btnMangaRecommendations.setBackgroundColor(buttonColor.rgb)
                btnMangaRelated.setBackgroundColor(buttonColor.rgb)
            }

            bindUI(mangaInfo)
        }
    }

    private fun bindUI(mangaInfo: Manga) {
        val listener = View.OnClickListener {
            showFullTitleDialog(mangaInfo)
        }

        binding.apply {

            tvMangaAnimeScore.text = mangaInfo.score.toString()
            tvMangaUsersAmount.text = mangaInfo.scored_by.toString()
            tvMangaPublishStartTime.text = if (mangaInfo.published.from != null) {
                mangaInfo.published.from.subSequence(0..9)
            } else {
                ""
            }
            tvMangaPublishingEnd.text = if (mangaInfo.published.to != null) {
                mangaInfo.published.to.subSequence(0..9)
            } else {
                ""
            }
            tvMangaTextScore.text = "Score:"
            tvMangaUsersText.text = "Users:"
            tvMangaMyScoreText.text = "My Score:"
            tvMangaTextChapters.text = "Chapters:"
            tvMangaVolumesText.text = "Volumes:"
            tvMangaPopularityText.text = "Popularity:"
            tvMangaStartedPublishingText.text = "Started Publishing:"
            tvMangaFinishedPublishingText.text = "Finished Publishing:"
            tvMangaSynopsisText.text = "Synopsis:"
            tvMangaGenresText.text = "Genres:"
            tvSerializationsText.text = "Serializations:"
            tvMangaMyScore.text = "--"
            btnMangaCharactersAndStaff.text = "Characters And Staff Information"
            btnMangaRecommendations.text = "Recommendations"
            btnMangaReviews.text = "Reviews"
            btnMangaRelated.text = "Related"

            tvMangaChapterCount.text = mangaInfo.chapters.toString()
            tvMangaVolumes.text = mangaInfo.volumes.toString()
            tvMangaPopularity.text = mangaInfo.popularity.toString()
            tvMangaNameEn.text = mangaInfo.title_english ?: mangaInfo.title
            tvMangaNameEn.setOnClickListener(listener)
            tvMangaNameJp.text = mangaInfo.title_japanese
            tvMangaNameJp.setOnClickListener(listener)
            tvMangaSynopsis.text = mangaInfo.synopsis

            tvMangaSerializations.text = viewModel.getSerializations(mangaInfo.serializations)
            tvMangaGenres.text = viewModel.getGenres(mangaInfo.genres)

            // buttons
            btnMangaCharactersAndStaff.setOnClickListener {
                Log.d(TAG, "bindUI: EEEEEEEEEEEEEEEEEEEEEEEEEEEEEE: $textBodyColor")
                val action =
                    MangaDetailsFragmentDirections.actionMangaDetailsFragmentToCharactersAndStaffFragment(
                        mangaInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        btnColor,
                        bgColor,
                        "manga"
                    )
                findNavController().navigate(action)
            }

            btnMangaRecommendations.setOnClickListener {
                val action =
                    MangaDetailsFragmentDirections.actionMangaDetailsFragmentToRecomendationsFragment(
                        mangaInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        bgColor,
                        false,
                        mangaInfo.title
                    )
                findNavController().navigate(action)
            }

            btnMangaReviews.setOnClickListener {
                val action =
                    MangaDetailsFragmentDirections.actionMangaDetailsFragmentToFragmentReviews(
                        mangaInfo.mal_id,
                        textTitleColor,
                        textBodyColor,
                        false,
                        bgColor
                    )
                findNavController().navigate(action)
            }

            btnMangaRelated.setOnClickListener {
                val action =
                    MangaDetailsFragmentDirections.actionMangaDetailsFragmentToRelatedFragment(
                        mangaInfo.related,
                        textTitleColor,
                        textBodyColor,
                        bgColor,
                        false
                    )
                findNavController().navigate(action)
            }

            ivMangaCover.setOnClickListener {
                // set up the observer
                viewModel.images.observe(viewLifecycleOwner, {
                    // create the dialog with the image data
                    if (it != null && !dialogState) {
                        val dialog =
                            PictureDetailsDialogFragment(it.pictures, this@MangaDetailsFragment)
                        dialog.show(parentFragmentManager, "dialog")
                        dialogState = true // lock the observer out of making anymore dialogs while one is open
                    }
                })

                // make the api call
                if(!apiState){
                    apiState = true
                    viewModel.getMangaImageData(args.malId)
                }
            }
        }
    }

    private fun showFullTitleDialog(mangaInfo: Manga) {
        // TODO ?? GET BETTER COLORS? Will need to set them programmatically not through style
        val englishTitle = mangaInfo.title_english ?: mangaInfo.title
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage(
                "English Title: \n\n"
                        + "$englishTitle \n\n\n\n"
                        + "Japanese Title: \n\n"
                        + "${mangaInfo.title_japanese} \n\n"
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