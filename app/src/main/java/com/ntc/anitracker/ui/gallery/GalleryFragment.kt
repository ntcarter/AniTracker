package com.ntc.anitracker.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.topanime.TopA
import com.ntc.anitracker.databinding.FragmentGalleryBinding
import com.ntc.anitracker.ui.adapters.GalleryAnimeAdapter
import com.ntc.anitracker.ui.adapters.GalleryMangaAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "GalleryFragment"

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    GalleryAnimeAdapter.OnItemClickListener,
    GalleryMangaAdapter.OnItemClickListener {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var animeAdapter: GalleryAnimeAdapter
    private lateinit var mangaAdapter: GalleryMangaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        setUpChipGroupListeners()

        // set default values
        viewModel.currentAnimeOption = binding.chipScore.id
        viewModel.currentMangaOption = binding.chipMangaScore.id

        animeAdapter = GalleryAnimeAdapter(this)
        mangaAdapter = GalleryMangaAdapter(this)

        binding.apply {
            rvGallery.setHasFixedSize(true)
            rvGallery.itemAnimator = null
            rvGallery.layoutManager = GridLayoutManager(requireContext(), 2)
            rvGallery.adapter = animeAdapter
        }

        viewModel.getAnimeGalleryData().observe(viewLifecycleOwner) {
            if (viewModel.animeActive) {
                animeAdapter.submitData(viewLifecycleOwner.lifecycle, it as PagingData<TopA>)
                binding.apply {
                    rvGallery.adapter = animeAdapter
                    rvGallery.scrollToPosition(0)
                }
            }
        }

        viewModel.getMangaGalleryData().observe(viewLifecycleOwner) {
            if (!viewModel.animeActive) {
                mangaAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.apply {
                    rvGallery.adapter = mangaAdapter
                    rvGallery.scrollToPosition(0)
                }
            }
        }

        // initial call to load data
        viewModel.getAnimeGalleryData()
    }

    private fun setUpChipGroupListeners() {
        binding.apply {

            // changes out the chip set based on the option selected
            chipGroupAnimeManga.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == chipAnime.id) {
                    setAnimeChips()
                } else if (checkedId == chipManga.id) {
                    setMangaChips()
                }
            }

            // changes the selected option
            chipGroupOptions.setOnCheckedChangeListener { _, checkedId ->
                if (chipAnime.isChecked) {
                    viewModel.currentAnimeOption = checkedId // tracks anime options selection
                } else { // manga chip selected
                    viewModel.currentMangaOption = checkedId // tracks manga options selection
                }

                val activeChip: Chip? = chipGroupOptions.findViewById(checkedId)
                if (activeChip != null) {
                    Log.d(TAG, "setUpChipGroupListeners: SETTING OPTIONS : ${activeChip.text} ")
                    viewModel.activeOption.value = activeChip.text.toString()

                    if (viewModel.animeActive) {
                        Log.d(TAG, "setUpChipGroupListeners: GETTING ANIME DATA HERE")
                        viewModel.getAnimeGalleryData()
                    } else {
                        Log.d(TAG, "setUpChipGroupListeners: GETTING MANGA DATA HERE")
                        viewModel.getMangaGalleryData()
                    }
                }
            }
        }
    }

    private fun setAnimeChips() {
        binding.apply {
            // anime chip options
            chipScore.visibility = View.VISIBLE
            chipPopularity.visibility = View.VISIBLE
            chipCurrentlyAiring.visibility = View.VISIBLE
            chipUpcoming.visibility = View.VISIBLE
            chipTvOnly.visibility = View.VISIBLE
            chipMovies.visibility = View.VISIBLE

            //manga chip options
            chipMangaScore.visibility = View.GONE
            chipMangaPopularity.visibility = View.GONE
            chipMangaOneShot.visibility = View.GONE
            chipMangaDoujin.visibility = View.GONE
            chipMangaLightNovel.visibility = View.GONE
            chipMangaNovel.visibility = View.GONE
            chipMangaManhwa.visibility = View.GONE

            viewModel.animeActive = true

            selectChip()
        }
    }

    private fun setMangaChips() {
        binding.apply {
            // anime chip options
            chipScore.visibility = View.GONE
            chipPopularity.visibility = View.GONE
            chipCurrentlyAiring.visibility = View.GONE
            chipUpcoming.visibility = View.GONE
            chipTvOnly.visibility = View.GONE
            chipMovies.visibility = View.GONE

            //manga chip options
            chipMangaScore.visibility = View.VISIBLE
            chipMangaPopularity.visibility = View.VISIBLE
            chipMangaOneShot.visibility = View.VISIBLE
            chipMangaDoujin.visibility = View.VISIBLE
            chipMangaLightNovel.visibility = View.VISIBLE
            chipMangaNovel.visibility = View.VISIBLE
            chipMangaManhwa.visibility = View.VISIBLE

            viewModel.animeActive = false

            selectChip()
        }
    }

    // sets/restores the active options chip when switching between anime and manga options
    private fun selectChip() {
        val activeAnimeChipId = viewModel.currentAnimeOption
        val activeMangaChipId = viewModel.currentMangaOption

        binding.apply {
            // get the active chips (the one currently selected and the one to be selected)
            val animeOptionChip = chipGroupOptions.findViewById<Chip>(activeAnimeChipId)
            val mangaOptionChip = chipGroupOptions.findViewById<Chip>(activeMangaChipId)

            if (binding.chipAnime.isChecked) {
                mangaOptionChip.isChecked = false
                animeOptionChip.isChecked = true
            } else {
                animeOptionChip.isChecked = false
                mangaOptionChip.isChecked = true
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}