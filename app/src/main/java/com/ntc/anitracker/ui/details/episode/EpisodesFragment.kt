package com.ntc.anitracker.ui.details.episode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntc.anitracker.MainActivity
import com.ntc.anitracker.R
import com.ntc.anitracker.databinding.FragmentEpisodesBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.EpisodesAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "EpisodesFragment"

@AndroidEntryPoint
class EpisodesFragment : Fragment(R.layout.fragment_episodes), MainActivity.ColorChanger {

    private val args by navArgs<EpisodesFragmentArgs>()

    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEpisodesBinding.bind(view)

        val adapter = EpisodesAdapter(args.titleTextColor, args.bodyTextColor)

        binding.apply {
            rvEpisodes.adapter = adapter
            rvEpisodes.setHasFixedSize(true)
            rvEpisodes.layoutManager = LinearLayoutManager(requireContext())
            rvEpisodes.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        viewModel.getEpisodeInformation(args.malId).observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        binding.clEpisodes.setBackgroundColor(args.bgColor)
        viewModel.getEpisodeInformation(args.malId)
    }

    override fun changeColors(isDefault: Boolean) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}