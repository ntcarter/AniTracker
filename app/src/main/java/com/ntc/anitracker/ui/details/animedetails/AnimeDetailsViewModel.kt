package com.ntc.anitracker.ui.details.animedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.anime.Anime
import com.ntc.anitracker.api.models.anime.Genre
import com.ntc.anitracker.api.models.anime.Studio
import com.ntc.anitracker.api.models.images.ImageData
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

    // image data
    private val _images = MutableLiveData<ImageData?>(null)
    val images: LiveData<ImageData?>
        get() = _images

    fun getAnimeData(malId: Int) {
        var anime: Anime?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                anime = repository.getAnimeDataFromJikan(malId)
                _anime.postValue(anime)
            } catch (e: HttpException) {
                Log.d(TAG, "getAnimeData: ERROR")
            }
        }
    }

    fun getAnimeImageData(malId: Int) {
        var images: ImageData?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                images = repository.getAnimeImageDataFromJikan(malId)
                _images.postValue(images)
            } catch (e: HttpException) {
                Log.d(TAG, "getAnimeData: ERROR")
            }
        }
    }

    fun getStudioList(studios: List<Studio>): String {
        var studiosResult = ""
        for (studio in studios) {
            if (studio != studios[studios.size - 1]) {
                studiosResult += "${studio.name}, "
            } else { // last element
                studiosResult += studio.name
            }
        }
        return studiosResult
    }

    fun getOpeningsList(openingThemes: List<String>): String {
        var openings = ""

        for (opening in openingThemes) {
            if (opening != openingThemes[openingThemes.size - 1]) {
                openings += "$opening, \n\n"
            } else { // last element
                openings += opening
            }
        }
        return openings
    }

    fun getGenreList(genresList: List<Genre>): String {
        var genresResult = ""
        for (genre in genresList) {
            if (genre.name != genresList[genresList.size - 1].name) {
                genresResult += "${genre.name}, "
            } else { // last element
                genresResult += genre.name
            }
        }
        return genresResult
    }


}