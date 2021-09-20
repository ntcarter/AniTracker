package com.ntc.anitracker.ui.details.animedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.anime.Anime
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "AnimeDetailsVM"

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    // current anime
    private val _anime = MutableLiveData<Anime?>(null)
    val anime: LiveData<Anime?>
        get() = _anime

    fun getAnimeData(malId: Int){
        var anime: Anime?
        viewModelScope.launch(Dispatchers.IO) {
            try{
                anime = repository.getAnimeDataFromJikan(malId)
                _anime.postValue(anime)
            }catch (e: HttpException){
                Log.d(TAG, "getAnimeData: ERROR")
            }
        }
    }
}