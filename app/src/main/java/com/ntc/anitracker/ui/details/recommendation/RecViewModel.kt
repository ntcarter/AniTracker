package com.ntc.anitracker.ui.details.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.recommendations.Recommendations
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RecViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    private val _recDetails = MutableLiveData<Recommendations?>(null)
    val recDetails: LiveData<Recommendations?>
        get() = _recDetails

    fun getRecommendations(malId: Int, isAnime: Boolean) {
        var recommendations: Recommendations?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                recommendations = if(isAnime){
                    repository.getAnimeRecommendations(malId)
                } else {
                    repository.getMangaRecommendations(malId)
                }
                _recDetails.postValue(recommendations)
            } catch (e: HttpException) {
            }
        }
    }
}