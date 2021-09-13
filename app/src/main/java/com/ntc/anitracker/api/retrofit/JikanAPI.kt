package com.ntc.anitracker.api.retrofit

import com.ntc.anitracker.api.models.topanime.TopAnime
import com.ntc.anitracker.api.models.topmanga.TopManga
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanAPI {

    companion object {
        const val BASE_URL = "https://api.jikan.moe"
    }

    @GET("/v3/top/anime/{page}")
    suspend fun getTopAnime(
        @Path("page") page: Int
    ): TopAnime

    @GET("/v3/top/anime/{page}/bypopularity")
    suspend fun getPopularAnime(
        @Path("page") page: Int
    ): TopAnime

    @GET("/v3/top/anime/{page}/airing")
    suspend fun getCurrentlyAiringAnime(
        @Path("page") page: Int
    ): TopAnime

    @GET("/v3/top/anime/{page}/upcoming")
    suspend fun getUpcomingAnime(
        @Path("page") page: Int
    ): TopAnime

    @GET("/v3/top/anime/{page}/tv")
    suspend fun getTVOnlyAnime(
        @Path("page") page: Int
    ): TopAnime

    @GET("/v3/top/anime/{page}/movie")
    suspend fun getMoviesAnime(
        @Path("page") page: Int
    ): TopAnime

    @GET("/v3/top/manga/{page}")
    suspend fun getTopManga(
        @Path("page") page: Int
    ): TopManga

    @GET("/v3/top/manga/{page}/bypopularity")
    suspend fun getPopularManga(
        @Path("page") page: Int
    ): TopManga

    @GET("/v3/top/manga/{page}/oneshots")
    suspend fun getOneShotsManga(
        @Path("page") page: Int
    ): TopManga

    @GET("/v3/top/manga/{page}/doujin")
    suspend fun getDoujinshiManga(
        @Path("page") page: Int
    ): TopManga

    @GET("/v3/top/manga/{page}/manhwa")
    suspend fun getManhwaManga(
        @Path("page") page: Int
    ): TopManga

    @GET("/v3/top/manga/{page}/novels")
    suspend fun getNovelsManga(
        @Path("page") page: Int
    ): TopManga

    @GET("/v3/top/manga/{page}/manhua")
    suspend fun getManhuaManga(
        @Path("page") page: Int
    ): TopManga


//    @GET("/anime/{id}")
//    suspend fun getAnimeData(
//        @Path("id") id: String
//    ): Anime
//
//    suspend fun searchAnime(
//        @Query("query") query: String,
//        @Query("page") page: Int,
//        @Query("per_page") perPage: Int
//    ): Anime
}