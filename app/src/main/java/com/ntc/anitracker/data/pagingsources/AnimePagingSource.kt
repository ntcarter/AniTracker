package com.ntc.anitracker.data.pagingsources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ntc.anitracker.api.models.topanime.TopA
import com.ntc.anitracker.data.JikanRepository
import retrofit2.HttpException
import java.io.IOException

private const val ANIME_STARTING_PAGE_INDEX = 1
private const val TAG = "AnimePagingSource"

class AnimePagingSource(
    private val activeOption: String,
    private val repository: JikanRepository
) : PagingSource<Int, TopA>(){


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopA> {
        val position = params.key ?: ANIME_STARTING_PAGE_INDEX

        return try {
            val response = repository.makeAnimeApiCall(activeOption, position)

            val results = response?.top ?: listOf()

            LoadResult.Page(
                data = results,
                // prev key is position -1 unless we are on the first page then there is no previous page
                prevKey = if (position == ANIME_STARTING_PAGE_INDEX) null else position - 1,
                // we know we are at the end of the results if the photos list is empty
                nextKey = if (results.isEmpty()) null else position + 1
            )
        }catch(e: IOException){
            Log.d(TAG, "load: ERROR: IO: $e")
            LoadResult.Error(e)
        }catch (e: HttpException){
            Log.d(TAG, "load: ERROR: HTTP: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopA>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}