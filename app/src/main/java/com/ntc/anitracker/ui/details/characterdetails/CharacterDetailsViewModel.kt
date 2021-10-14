package com.ntc.anitracker.ui.details.characterdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.characterdetails.CharacterDetails
import com.ntc.anitracker.api.models.images.ImageData
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject



@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    private val _characterDetails = MutableLiveData<CharacterDetails?>(null)
    val characterDetails: LiveData<CharacterDetails?>
        get() = _characterDetails

    // image data
    private val _images = MutableLiveData<ImageData?>(null)
    val images: LiveData<ImageData?>
        get() = _images

    fun getCharacterDetails(characterId: Int){
        var details: CharacterDetails?
        viewModelScope.launch(Dispatchers.IO) {
            try{
                details = repository.getCharacterDetails(characterId)
                _characterDetails.postValue(details)
            }catch (e: HttpException){}
        }
    }

    fun getCharacterImageData(malId: Int) {
        var images: ImageData?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                images = repository.getCharacterImageDataFromJikan(malId)
                _images.postValue(images)
            } catch (e: HttpException) {
            }
        }
    }
}