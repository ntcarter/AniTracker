package com.ntc.anitracker.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.topanime.TopA
import com.ntc.anitracker.api.models.topmanga.TopM
import com.ntc.anitracker.databinding.FragmentGalleryBinding
import com.ntc.anitracker.ui.adapters.galleryadapters.GalleryAnimeAdapter
import com.ntc.anitracker.ui.adapters.galleryadapters.GalleryMangaAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "GalleryFragment"
private const val TAP_LOCK_TIME_MILLISECONDS = 1500

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    GalleryAnimeAdapter.OnItemClickListener,
    GalleryMangaAdapter.OnItemClickListener {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var animeAdapter: GalleryAnimeAdapter
    private lateinit var mangaAdapter: GalleryMangaAdapter

    // Keeps track of time since resumed to lock the user out of a tap for 1.5 seconds
    private var timeResumed: Long = 0L

    private var coverTapLock = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        // check default values
        if (viewModel.currentAnimeOption == 0) {
            viewModel.currentAnimeOption = binding.chipScore.id
            binding.chipAnime.isChecked = true
        }
        if (viewModel.currentMangaOption == 0) {
            viewModel.currentMangaOption = binding.chipMangaScore.id
        }

        setUpChipGroupListeners()

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
                animeAdapter.submitData(viewLifecycleOwner.lifecycle, it)
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

    override fun onResume() {
        timeResumed = System.currentTimeMillis()
        coverTapLock = false
        super.onResume()
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
                    Log.d(TAG, "setUpChipGroupListeners: SETTING ANIME OPTION")
                    viewModel.currentAnimeOption = checkedId // tracks anime options selection
                } else if (chipManga.isChecked) { // manga chip selected
                    Log.d(TAG, "setUpChipGroupListeners: SETTING MANGA OPTION")
                    viewModel.currentMangaOption = checkedId // tracks manga options selection
                }

                val activeChip: Chip? = chipGroupOptions.findViewById(checkedId)
                if (activeChip != null) {
                    viewModel.activeOption.value = activeChip.text.toString()
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
                Log.d(TAG, "selectChip: switching to anime chip: ${animeOptionChip.text}")
                mangaOptionChip.isChecked = false
                animeOptionChip.isChecked = true
            } else {
                Log.d(TAG, "selectChip: switching to manga chip ${mangaOptionChip.text}")
                animeOptionChip.isChecked = false
                mangaOptionChip.isChecked = true
            }
        }
    }

    override fun onAnimeCoverClick(anime: TopA) {
        if (!coverTapLock) {
            coverTapLock = true
            lifecycleScope.launch {
                var currentTime = System.currentTimeMillis()
                // The API is a little slow so delay a users quick tap
                while (currentTime - timeResumed < TAP_LOCK_TIME_MILLISECONDS) {
                    delay(1000)
                    currentTime = System.currentTimeMillis()
                }
                // pass the anime to the details fragment. It will handle making further api calls
                val action =
                    GalleryFragmentDirections.actionGalleryFragmentToAnimeDetailsFragment(anime.mal_id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onMangaCoverClick(manga: TopM) {
        if (!coverTapLock) {
            coverTapLock = true
            lifecycleScope.launch {
                var currentTime = System.currentTimeMillis()
                // The API is a little slow so delay a users quick tap
                while (currentTime - timeResumed < TAP_LOCK_TIME_MILLISECONDS) {
                    delay(1000)
                    currentTime = System.currentTimeMillis()
                }
                // pass the anime to the details fragment. It will handle making further api calls
                val action =
                    GalleryFragmentDirections.actionGalleryFragmentToMangaDetailsFragment(manga.mal_id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}