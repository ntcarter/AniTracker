package com.ntc.anitracker.api.retrofit

import com.ntc.anitracker.api.models.anime.Anime
import com.ntc.anitracker.api.models.characterdetails.CharacterDetails
import com.ntc.anitracker.api.models.charactersandstaff.CharactersAndStaff
import com.ntc.anitracker.api.models.episode.EpisodeInfo
import com.ntc.anitracker.api.models.images.ImageData
import com.ntc.anitracker.api.models.manga.Manga
import com.ntc.anitracker.api.models.person.Person
import com.ntc.anitracker.api.models.recommendations.Recommendations
import com.ntc.anitracker.api.models.reviews.Reviews
import com.ntc.anitracker.api.models.topanime.TopAnime
import com.ntc.anitracker.api.models.topmanga.TopManga
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanAPI {

    companion object {
        const val BASE_URL = "https://api.jikan.moe"
    }

    // anime API calls

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

    @GET("/v3/anime/{id}")
    suspend fun getAnimeData(
        @Path("id") id: Int
    ): Anime

    @GET("/v3/anime/{id}/pictures")
    suspend fun getAnimeImageData(
        @Path("id") id: Int
    ): ImageData

    @GET("/v3/anime/{id}/recommendations")
    suspend fun getAnimeRecommendations(
        @Path("id") id: Int
    ): Recommendations

    @GET("https://api.jikan.moe/v3/anime/{id}/episodes/{page}")
    suspend fun getEpisodeInformation(
        @Path("page") page: Int,
        @Path("id") id: Int
    ): EpisodeInfo


    @GET("https://api.jikan.moe/v3/anime/{id}/reviews/{page}")
    suspend fun getAnimeReviews(
        @Path("page") page: Int,
        @Path("id") id: Int
    ): Reviews

    // Manga API calls
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

    @GET("/v3/manga/{id}")
    suspend fun getMangaData(
        @Path("id") id: Int
    ): Manga

    @GET("/v3/manga/{id}/pictures")
    suspend fun getMangaImageData(
        @Path("id") id: Int
    ): ImageData

    @GET("/v3/manga/{id}/recommendations")
    suspend fun getMangaRecommendations(
        @Path("id") id: Int
    ): Recommendations

    @GET("https://api.jikan.moe/v3/manga/{id}/reviews/{page}")
    suspend fun getMangaReviews(
        @Path("page") page: Int,
        @Path("id") id: Int
    ): Reviews

    // Characters and Staff
    @GET("/v3/anime/{id}/characters_staff")
    suspend fun getAnimeCharacters(
        @Path("id") id: Int
    ): CharactersAndStaff

    @GET("/v3/manga/{id}/characters")
    suspend fun getMangaCharacters(
        @Path("id") id: Int
    ): CharactersAndStaff

    @GET("/v3/character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): CharacterDetails

    @GET("/v3/person/{id}")
    suspend fun getPersonDetails(
        @Path("id") id: Int
    ): Person

    @GET("/v3/person/{id}/pictures")
    suspend fun getPersonImageData(
        @Path("id") id: Int
    ): ImageData

    @GET("/v3/character/{id}/pictures")
    suspend fun getCharacterImageData(
        @Path("id") id: Int
    ): ImageData

//    suspend fun searchAnime(
//        @Query("query") query: String,
//        @Query("page") page: Int,
//        @Query("per_page") perPage: Int
//    ): Anime
}