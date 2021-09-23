package com.ntc.anitracker.data.pagingsources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ntc.anitracker.api.models.episode.Episode
import com.ntc.anitracker.api.retrofit.JikanAPI
import retrofit2.HttpException
import java.io.IOException

private const val EPISODE_STARTING_PAGE_INDEX = 1
private const val TAG = "EpisodePagingSource"
class EpisodePagingSource(
    val api: JikanAPI,
    val malId: Int
) : PagingSource<Int, Episode>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        val position = params.key ?: EPISODE_STARTING_PAGE_INDEX

        return try {
            Log.d(TAG, "load: CALLING THE API IN PAGING SOURCE")
            val response = api.getEpisodeInformation(position, malId)

            val results = response.episodes ?: listOf()

            LoadResult.Page(
                data = results,
                // prev key is position -1 unless we are on the first page then there is no previous page
                prevKey = if (position == EPISODE_STARTING_PAGE_INDEX) null else position - 1,
                // we know we are at the end of the results if the photos list is empty
                nextKey = if (results.isEmpty()) null else position + 1
            )
        } catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}