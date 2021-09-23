package com.ntc.anitracker.ui.details.persondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.characterdetails.CharacterDetails
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

//@HiltViewModel
//class PersonDetailsViewModel @Inject constructor(
//    private val repository: JikanRepository
//) : ViewModel(){
//
//    private val _personDetails = MutableLiveData<?>(null)
//    val personDetails: LiveData<CharacterDetails?>
//        get() = _personDetails
//
//    fun getCharacterDetails(characterId: Int){
//        var details: CharacterDetails?
//        viewModelScope.launch(Dispatchers.IO) {
//            try{
//                details = repository.getCharacterDetails(characterId)
//                _characterDetails.postValue(details)
//            }catch (e: HttpException){}
//        }
//    }
//}