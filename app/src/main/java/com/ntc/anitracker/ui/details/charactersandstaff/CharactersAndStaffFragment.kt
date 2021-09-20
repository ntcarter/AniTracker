package com.ntc.anitracker.ui.details.charactersandstaff

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.charactersandstaff.Character
import com.ntc.anitracker.api.models.charactersandstaff.CharacterStaff
import com.ntc.anitracker.api.models.charactersandstaff.CharactersAndStaff
import com.ntc.anitracker.databinding.FragmentCharactersStaffBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.CharactersAndStaffAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersAndStaffFragment : Fragment(R.layout.fragment_characters_staff) {

    private val args by navArgs<CharactersAndStaffFragmentArgs>()

    private var _binding: FragmentCharactersStaffBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersAndStaffViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharactersStaffBinding.bind(view)

        viewModel.characterInfo.observe(viewLifecycleOwner, { characterInfo ->
            characterInfo?.let {
                bindUi(characterInfo)
            }
        })

        binding.clDetails.setBackgroundColor(args.bgColor) // set Background color early to avoid white screen flashes
        viewModel.getCharactersAndStaffInfo(args.animeId)
    }

    private fun bindUi(characterInfo: CharactersAndStaff) {
        binding.apply {
            // combine the list of character and list of staff into one list for the adapter
            val infoList = getCharStaffList(characterInfo)
            val adapter = CharactersAndStaffAdapter(infoList, args.titleTextColor)
            rvCandS.adapter = adapter
            rvCandS.setHasFixedSize(true)
            rvCandS.layoutManager = LinearLayoutManager(requireContext())
            rvCandS.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun getCharStaffList(charInfo: CharactersAndStaff): List<CharacterStaff> {
        val list: MutableList<CharacterStaff> = mutableListOf()
        for(character in charInfo.characters){
            list.add(CharacterStaff(character.image_url, character.name, character.role, character.voice_actors, null))
        }
        for(staff in charInfo.staff){
            list.add(CharacterStaff(staff.image_url, staff.name, null, null, staff.positions))
        }

        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}