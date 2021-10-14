package com.ntc.anitracker.ui.details.characterdetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.characterdetails.CharacterDetails
import com.ntc.anitracker.databinding.FragmentCharacterDetailsBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.VoiceActorAdapter
import com.ntc.anitracker.ui.adapters.detailsadapters.oGraphyAdapter
import com.ntc.anitracker.ui.details.dialog.PictureDetailsDialogFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CharacterDetails"

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details),
    oGraphyAdapter.OnOgraphyClick, VoiceActorAdapter.OnVoiceClick,
    PictureDetailsDialogFragment.OnDialogState {

    private val args by navArgs<CharacterDetailsFragmentArgs>()

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailsViewModel by viewModels()

    // Holds the state of an active image dialog. If true there's an active dialog currently showing
    private var dialogState = false
    private var apiState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharacterDetailsBinding.bind(view)

        viewModel.characterDetails.observe(viewLifecycleOwner, { characterDetails ->
            characterDetails?.let {
                //Bind ui with the data
                bindUi(characterDetails)
            }
        })

        updateDefaultUI() // Default UI needs to be changed before api call to avoid flashing color changes

        viewModel.getCharacterDetails(args.characterId)
    }

    private fun bindUi(characterDetails: CharacterDetails) {
        binding.apply {
            Glide.with(requireActivity())
                .load(characterDetails.image_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivChar)

            tvNameEN.text = characterDetails.name
            tvNameEN.setTextColor(args.titleTextColor)

            if (characterDetails.name_kanji != null) {
                tvNameJp.text = "(${characterDetails.name_kanji})"
                tvNameJp.setTextColor(args.titleTextColor)
            }

            tvCharDescription.text = characterDetails.about
            tvCharDescription.setTextColor(args.bodyTextColor)

            tvCharEmptyVA.setTextColor(args.titleTextColor)

            if (!characterDetails.voice_actors.isNullOrEmpty()) {
                // Voice actor recyclerView
                val voiceActorAdapter =
                    VoiceActorAdapter(
                        characterDetails.voice_actors,
                        args.titleTextColor,
                        this@CharacterDetailsFragment
                    )
                rvVoiceActor.layoutManager = LinearLayoutManager(requireContext())
                rvVoiceActor.setHasFixedSize(true)
                rvVoiceActor.adapter = voiceActorAdapter
                rvVoiceActor.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                rvVoiceActor.visibility = View.VISIBLE
                tvCharEmptyVA.visibility = View.INVISIBLE
            } else {
                rvVoiceActor.visibility = View.INVISIBLE
                tvCharEmptyVA.visibility = View.VISIBLE
            }


            // animeography/mangaography recyclerView
            val animeAdapter = oGraphyAdapter(
                characterDetails.animeography,
                args.titleTextColor,
                this@CharacterDetailsFragment,
                true
            )

            val mangaAdapter = oGraphyAdapter(
                characterDetails.mangaography,
                args.titleTextColor,
                this@CharacterDetailsFragment,
                false
            )

            rvOgraphy.layoutManager = LinearLayoutManager(requireContext())
            rvOgraphy.setHasFixedSize(true)
            rvOgraphy.adapter = animeAdapter // setting default selection
            rvOgraphy.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            cgCharacterDetails.setOnCheckedChangeListener { _, _ ->
                if (chipAnimeography.isChecked) {
                    rvOgraphy.adapter = animeAdapter
                } else {
                    rvOgraphy.adapter = mangaAdapter
                }
            }

            ivChar.setOnClickListener {
                viewModel.images.observe(viewLifecycleOwner, {
                    // create the dialog with the image data
                    if (it != null && !dialogState) {
                        val dialog =
                            PictureDetailsDialogFragment(it.pictures, this@CharacterDetailsFragment)
                        dialog.show(parentFragmentManager, "dialog")
                        dialogState =
                            true // lock the observer out of making anymore dialogs while one is open
                    }
                })

                // make the api call
                if (!apiState) {
                    apiState = true
                    viewModel.getCharacterImageData(args.characterId)
                }
            }
        }
    }

    private fun updateDefaultUI() {

        val bgColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked),
            ), intArrayOf(
                args.buttonColor,
                args.bodyTextColor
            )
        )
        val textColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked),
            ), intArrayOf(
                args.titleTextColor,
                args.buttonColor
            )
        )

        binding.apply {
            clBG.setBackgroundColor(args.bgColor)

            chipAnimeography.chipBackgroundColor = bgColorStateList
            chipAnimeography.setTextColor(textColorStateList)

            chipMangaography.chipBackgroundColor = bgColorStateList
            chipMangaography.setTextColor(textColorStateList)
        }
    }

    override fun onMangaographyCLick(malId: Int) {
        val action =
            CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToMangaDetailsFragment(
                malId
            )
        findNavController().navigate(action)
    }

    override fun onAnimeographyCLick(malId: Int) {
        val action =
            CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToAnimeDetailsFragment(
                malId
            )
        findNavController().navigate(action)
    }

    override fun onVoiceActorClick(personId: Int) {
        val action =
            CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToPersonDetailsFragment(
                personId,
                args.titleTextColor,
                args.bodyTextColor,
                args.bgColor,
                args.buttonColor
            )
        findNavController().navigate(action)
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