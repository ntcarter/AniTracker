package com.ntc.anitracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ntc.anitracker.api.models.episode.Episode
import com.ntc.anitracker.api.models.topanime.TopA
import com.ntc.anitracker.api.models.topanime.TopAnime
import com.ntc.anitracker.api.models.topmanga.TopM
import com.ntc.anitracker.api.models.topmanga.TopManga
import com.ntc.anitracker.api.retrofit.JikanAPI
import com.ntc.anitracker.data.pagingsources.AnimePagingSource
import com.ntc.anitracker.data.pagingsources.EpisodePagingSource
import com.ntc.anitracker.data.pagingsources.MangaPagingSource
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "JikanRepository"

@Singleton
class JikanRepository @Inject constructor(private val jikanAPI: JikanAPI) {

    fun getAnimeGalleryData(activeOption: String): LiveData<PagingData<TopA>> {
        return Pager(
            config = PagingConfig(
                pageSize = 24, // this will later be passed as our params.loadsize value in the PagingSource
                maxSize = 90,
                enablePlaceholders = false,
                prefetchDistance = 32
            ),
            pagingSourceFactory = { AnimePagingSource(activeOption, this) }
        ).liveData
    }

    fun getMangaGalleryData(activeOption: String): LiveData<PagingData<TopM>> {
        return Pager(
            config = PagingConfig(
                pageSize = 24, // this will later be passed as our params.loadsize value in the PagingSource
                maxSize = 90,
                enablePlaceholders = false,
                prefetchDistance = 32
            ),
            pagingSourceFactory = { MangaPagingSource(activeOption, this) }
        ).liveData
    }

    fun getEpisodeInformation(malId: Int): LiveData<PagingData<Episode>> {
        Log.d(TAG, "getEpisodeInformation: IN GETEPISODE PAGER!!")
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                maxSize = 150,
                enablePlaceholders = false,
                prefetchDistance = 50
            ),
            pagingSourceFactory = { EpisodePagingSource(jikanAPI, malId) }
        ).liveData
    }

    suspend fun makeAnimeApiCall(activeOption: String, position: Int): TopAnime? {
        var resultAnime: TopAnime? = null

        try {
            // anime calls
            when (activeOption) {
                "Score" -> {
                    resultAnime = jikanAPI.getTopAnime(position)
                }
                "Popularity" -> {
                    resultAnime = jikanAPI.getPopularAnime(position)
                }
                "Currently Airing" -> {
                    resultAnime = jikanAPI.getCurrentlyAiringAnime(position)
                }
                "Upcoming" -> {
                    resultAnime = jikanAPI.getUpcomingAnime(position)
                }
                "TV Only" -> {
                    resultAnime = jikanAPI.getTVOnlyAnime(position)
                }
                "Movies" -> {
                    resultAnime = jikanAPI.getMoviesAnime(position)
                }
            }
        } catch (e: HttpException) {
            Log.d(TAG, "makeJikanApiCall: ERROR HTTP $e")
        }

        return resultAnime
    }

    suspend fun makeMangaApiCall(activeOption: String, position: Int): TopManga? {
        Log.d(TAG, "makeJikanApiCall: Manga start")
        var resultManga: TopManga? = null

        try {
            //manga calls
            when (activeOption) {
                "Top Manga" -> {
                    resultManga = jikanAPI.getTopManga(position)
                }
                "Most Popular" -> {
                    resultManga = jikanAPI.getPopularManga(position)
                }
                "One-Shots" -> {
                    resultManga = jikanAPI.getOneShotsManga(position)
                }
                "Doujinshi" -> {
                    resultManga = jikanAPI.getDoujinshiManga(position)
                }
                "manhua" -> {
                    resultManga = jikanAPI.getManhuaManga(position)
                }
                "Novels" -> {
                    resultManga = jikanAPI.getNovelsManga(position)
                }
                "Manhwa" -> {
                    resultManga = jikanAPI.getManhwaManga(position)
                }
            }
        } catch (e: HttpException) {
            Log.d(TAG, "makeJikanApiCall: ERROR HTTP $e")
        }

        return resultManga
    }

    suspend fun getAnimeDataFromJikan(malId: Int) = jikanAPI.getAnimeData(malId)

    suspend fun getAnimeCharacters(malId: Int) = jikanAPI.getAnimeCharacters(malId)

    suspend fun getCharacterDetails(characterId: Int) = jikanAPI.getCharacterDetails(characterId)

    suspend fun getRecommendations(malId: Int) = jikanAPI.getRecommendations(malId)

}