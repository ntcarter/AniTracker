package com.ntc.anitracker.ui.details.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val repository: JikanRepository
): ViewModel(){

    fun getReviews(malId: Int, isAnime: Boolean) = repository.getReviews(malId, isAnime).cachedIn(viewModelScope)
}