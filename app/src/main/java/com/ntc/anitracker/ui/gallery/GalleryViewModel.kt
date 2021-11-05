package com.ntc.anitracker.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "GalleryViewModel"
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel(){

    var currentAnimeOption: Int = 0
    var currentMangaOption: Int = 0

    //true for anime chips active false for manga
    var animeActive = true

    // tracks the active option selected by the user
    val activeOption = MutableLiveData("Score")

    var pageNumber = 1;

    fun getAnimeGalleryData() = activeOption.switchMap {
        repository.getAnimeGalleryData(activeOption.value!!).cachedIn(viewModelScope)
    }

    fun getMangaGalleryData() = activeOption.switchMap {
        repository.getMangaGalleryData(activeOption.value!!).cachedIn(viewModelScope)
    }

}