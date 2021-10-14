package com.ntc.anitracker.ui.details.mangadetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.images.ImageData
import com.ntc.anitracker.api.models.manga.Genre
import com.ntc.anitracker.api.models.manga.Manga
import com.ntc.anitracker.api.models.manga.Serialization
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "AnimeDetailsVM"

@HiltViewModel
class MangaDetailsViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    // current anime
    private val _manga = MutableLiveData<Manga?>(null)
    val manga: LiveData<Manga?>
        get() = _manga

    // image data
    private val _images = MutableLiveData<ImageData?>(null)
    val images: LiveData<ImageData?>
        get() = _images

    fun getMangaData(malId: Int) {
        var manga: Manga?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                manga = repository.getMangaDataFromJikan(malId)
                _manga.postValue(manga)
            } catch (e: HttpException) {
                Log.d(TAG, "getAnimeData: ERROR")
            }
        }
    }

    fun getMangaImageData(malId: Int) {
        var images: ImageData?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                images = repository.getMangaImageDataFromJikan(malId)
                _images.postValue(images)
            } catch (e: HttpException) { }
        }
    }

    fun getSerializations(serializations: List<Serialization>): String {
        var serializationsResult = ""
        for (serialization in serializations) {
            if (serialization.name != serializations[serializations.size - 1].name) {
                serializationsResult += "${serialization.name}, "
            } else { // last element
                serializationsResult += serialization.name
            }
        }

        return serializationsResult
    }

    fun getGenres(genres: List<Genre>): String {
        var genresResult = ""
        for (genre in genres) {
            if (genre.name != genres[genres.size - 1].name) {
                genresResult += "${genre.name}, "
            } else { // last element
                genresResult += genre.name
            }
        }
        return genresResult
    }
}