package com.ntc.anitracker.ui.details.persondetails

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
import com.ntc.anitracker.api.models.person.Person
import com.ntc.anitracker.databinding.FragmentPersonDetailsBinding
import com.ntc.anitracker.ui.adapters.detailsadapters.AnimeStaffPositionsAdapter
import com.ntc.anitracker.ui.adapters.detailsadapters.PublishedMangaAdapter
import com.ntc.anitracker.ui.adapters.detailsadapters.VoiceRolesAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PersonDetailsFragment"

@AndroidEntryPoint
class PersonDetailsFragment : Fragment(R.layout.fragment_person_details),
    VoiceRolesAdapter.OnPersonDetailsClick {

    private val args by navArgs<PersonDetailsFragmentArgs>()

    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PersonDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPersonDetailsBinding.bind(view)

        viewModel.personDetails.observe(viewLifecycleOwner, { person ->
            person?.let {
                bindUi(person)
            }
        })

        updateDefaultUi() // Default UI needs to be changed before api call to avoid flashing color changes

        viewModel.getPersonDetails(args.personId)
    }

    private fun bindUi(person: Person) {
        binding.apply {
            Glide.with(requireActivity())
                .load(person.image_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivPersonDetails)

            tvPersonDetailsName.text = person.name
            tvPersonDetailsName.setTextColor(args.titleTextColor)
            tvEmpty.setTextColor(args.titleTextColor)

            if (person.alternate_names != null) {
                tvPersonAltNames.text =
                    "Alternate Names: \n" + person.alternate_names.toString() + "\n"
                tvPersonAltNames.visibility = View.VISIBLE
                tvPersonAltNames.setTextColor(args.bodyTextColor)
            } else {
                tvPersonAltNames.visibility = View.GONE
            }

            if (person.birthday != null) {
                tvPersonBirthday.text = "Birthday: \n" + person.birthday.substring(0..9) + "\n"
                tvPersonBirthday.visibility = View.VISIBLE
                tvPersonBirthday.setTextColor(args.bodyTextColor)
            } else {
                tvPersonBirthday.visibility = View.GONE
            }

            if (person.family_name != null) {
                tvPersonFamilyName.text = "Family Name:\n" + person.family_name + "\n"
                tvPersonFamilyName.visibility = View.VISIBLE
                tvPersonFamilyName.setTextColor(args.bodyTextColor)
            } else {
                tvPersonFamilyName.visibility = View.GONE
            }

            if (person.given_name != null) {
                tvPersonGivenName.text = "Given Name:\n" + person.family_name + "\n"
                tvPersonGivenName.visibility = View.VISIBLE
                tvPersonGivenName.setTextColor(args.bodyTextColor)
            } else {
                tvPersonGivenName.visibility = View.GONE
            }

            if (person.about != null) {
                tvPersonDetailsAbout.text = "About:\n" + person.family_name
                tvPersonDetailsAbout.visibility = View.VISIBLE
                tvPersonDetailsAbout.setTextColor(args.bodyTextColor)
            } else {
                tvPersonDetailsAbout.visibility = View.GONE
            }

            rvPersonDetails.setHasFixedSize(true)
            rvPersonDetails.layoutManager = LinearLayoutManager(requireContext())
            rvPersonDetails.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            //Default adapter
            if (!person.voice_acting_roles.isNullOrEmpty()) {
                val voiceRolesAdapter =
                    VoiceRolesAdapter(
                        person.voice_acting_roles,
                        args.bodyTextColor,
                        this@PersonDetailsFragment
                    )
                rvPersonDetails.adapter = voiceRolesAdapter
                tvEmpty.visibility = View.GONE
                rvPersonDetails.visibility = View.VISIBLE
            } else {
                rvPersonDetails.visibility = View.INVISIBLE
                tvEmpty.text = "This Person Has No VoiceActingRoles"
                tvEmpty.visibility = View.VISIBLE
            }

            cgPersonDetails.setOnCheckedChangeListener { _, _ ->
                if (chipAnimePositons.isChecked) {
                    if (!person.anime_staff_positions.isNullOrEmpty()) {
                        val animeStaffPositionsAdapter =
                            AnimeStaffPositionsAdapter(
                                person.anime_staff_positions,
                                args.bodyTextColor,
                                this@PersonDetailsFragment
                            )
                        rvPersonDetails.adapter = animeStaffPositionsAdapter
                        tvEmpty.visibility = View.GONE
                        rvPersonDetails.visibility = View.VISIBLE
                    } else {
                        rvPersonDetails.visibility = View.INVISIBLE
                        tvEmpty.text = "This Person Has No Anime Staff Positions"
                        tvEmpty.visibility = View.VISIBLE
                    }
                } else if (chipPublishedManga.isChecked) {
                    if (!person.published_manga.isNullOrEmpty()) {
                        val publishedMangaAdapter =
                            PublishedMangaAdapter(
                                person.published_manga,
                                args.bodyTextColor,
                                this@PersonDetailsFragment
                            )
                        rvPersonDetails.adapter = publishedMangaAdapter
                        tvEmpty.visibility = View.GONE
                        rvPersonDetails.visibility = View.VISIBLE
                    } else {
                        rvPersonDetails.visibility = View.INVISIBLE
                        tvEmpty.text = "This Person Has No Published Manga"
                        tvEmpty.visibility = View.VISIBLE
                    }

                } else {
                    if (!person.voice_acting_roles.isNullOrEmpty()) {
                        val voiceRolesAdapter =
                            VoiceRolesAdapter(
                                person.voice_acting_roles,
                                args.bodyTextColor,
                                this@PersonDetailsFragment
                            )
                        rvPersonDetails.adapter = voiceRolesAdapter
                        tvEmpty.visibility = View.GONE
                        rvPersonDetails.visibility = View.VISIBLE
                    } else {
                        rvPersonDetails.visibility = View.INVISIBLE
                        tvEmpty.text = "This Person Has No VoiceActingRoles"
                        tvEmpty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateDefaultUi() {
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
            clPersonDetails.setBackgroundColor(args.bgColor)

            chipAnimePositons.chipBackgroundColor = bgColorStateList
            chipAnimePositons.setTextColor(textColorStateList)

            chipPublishedManga.chipBackgroundColor = bgColorStateList
            chipPublishedManga.setTextColor(textColorStateList)

            chipVARoles.chipBackgroundColor = bgColorStateList
            chipVARoles.setTextColor(textColorStateList)
        }
    }

    override fun onAnimeClick(malId: Int) {
        val action =
            PersonDetailsFragmentDirections.actionPersonDetailsFragmentToAnimeDetailsFragment(
                malId
            )
        findNavController().navigate(action)
    }

    override fun onCharacterClick(malId: Int) {
        val action =
            PersonDetailsFragmentDirections.actionPersonDetailsFragmentToCharacterDetailsFragment(
                malId,
                args.titleTextColor,
                args.bodyTextColor,
                args.bgColor,
                args.buttonColor
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}