package com.ntc.anitracker.ui.details.persondetails

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ntc.anitracker.R
import com.ntc.anitracker.databinding.FragmentPersonDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PersonDetailsFragment"

@AndroidEntryPoint
class PersonDetailsFragment : Fragment(R.layout.fragment_person_details) {
    //TODO implementation API BUGGED AS OF 9/22
    private val args by navArgs<PersonDetailsFragmentArgs>()

    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}