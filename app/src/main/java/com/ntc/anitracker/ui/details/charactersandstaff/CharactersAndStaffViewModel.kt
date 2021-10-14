package com.ntc.anitracker.ui.details.charactersandstaff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntc.anitracker.api.models.charactersandstaff.CharactersAndStaff
import com.ntc.anitracker.data.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "CandSInfo"

@HiltViewModel
class CharactersAndStaffViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    private val _characterInfo = MutableLiveData<CharactersAndStaff?>(null)
    val characterInfo: LiveData<CharactersAndStaff?>
        get() = _characterInfo

    fun getAnimeCharactersAndStaffInfo(malId: Int) {
        var info: CharactersAndStaff?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                info = repository.getAnimeCharacters(malId)
                _characterInfo.postValue(info)
            } catch (e: HttpException) {
                Log.d(TAG, "getCharactersAndStaffInfo: ERROR")
            }
        }
    }

    fun getMangaCharactersAndStaffInfo(malId: Int) {
        var info: CharactersAndStaff?
        viewModelScope.launch(Dispatchers.IO) {
            try {
                info = repository.getMangaCharacters(malId)
                _characterInfo.postValue(info)
            } catch (e: HttpException) {
                Log.d(TAG, "getCharactersAndStaffInfo: ERROR")
            }
        }
    }
}