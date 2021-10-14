package com.ntc.anitracker.ui.details.persondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.images.ImageData
import com.ntc.anitracker.api.models.person.Person
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    private val _personDetails = MutableLiveData<Person>(null)
    val personDetails: LiveData<Person?>
        get() = _personDetails

    // image data
    private val _images = MutableLiveData<ImageData?>(null)
    val images: LiveData<ImageData?>
        get() = _images

    fun getPersonDetails(personId: Int) {
        var details: Person?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                details = repository.getPersonDetails(personId)

                _personDetails.postValue(details!!)
            } catch (e: HttpException) {
            }
        }
    }

    fun getPersonImageData(malId: Int) {
        var images: ImageData?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                images = repository.getPersonImageDataFromJikan(malId)
                _images.postValue(images)
            } catch (e: HttpException) {
            }
        }
    }
}