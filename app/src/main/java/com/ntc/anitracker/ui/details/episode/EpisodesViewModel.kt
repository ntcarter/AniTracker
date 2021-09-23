package com.ntc.anitracker.ui.details.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

   fun getEpisodeInformation(malId: Int) = repository.getEpisodeInformation(malId).cachedIn(viewModelScope)
}