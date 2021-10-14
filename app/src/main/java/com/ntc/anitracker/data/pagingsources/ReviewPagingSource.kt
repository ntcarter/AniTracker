package com.ntc.anitracker.data.pagingsources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ntc.anitracker.api.models.reviews.Review
import com.ntc.anitracker.api.retrofit.JikanAPI
import retrofit2.HttpException
import java.io.IOException

private const val REVIEW_STARTING_PAGE_INDEX = 1
private const val TAG = "ReviewPagingSource"
class ReviewPagingSource(
    val api: JikanAPI,
    val malId: Int,
    val isAnime: Boolean
) : PagingSource<Int, Review>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val position = params.key ?: REVIEW_STARTING_PAGE_INDEX

        return try {

            val response = if (isAnime) {
                Log.d(TAG, "load: ANIME REVIEWS")
                api.getAnimeReviews(position, malId)
            } else {
                Log.d(TAG, "load: MANGAREVIEWS")
                api.getMangaReviews(position, malId)
            }

            val results = response.reviews ?: listOf()

            LoadResult.Page(
                data = results,
                // prev key is position -1 unless we are on the first page then there is no previous page
                prevKey = if (position == REVIEW_STARTING_PAGE_INDEX) null else position - 1,
                // we know we are at the end of the results if the photos list is empty
                nextKey = if (results.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}