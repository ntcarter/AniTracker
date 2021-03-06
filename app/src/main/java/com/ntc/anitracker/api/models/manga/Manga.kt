package com.ntc.anitracker.api.models.manga

import com.ntc.anitracker.api.models.anime.Related


data class Manga(
    val authors: List<Author>,
    val background: String,
    val chapters: Int?,
    val demographics: List<Demographic>,
    val explicit_genres: List<Any>,
    val external_links: List<ExternalLink>,
    val favorites: Int,
    val genres: List<Genre>,
    val image_url: String,
    val mal_id: Int,
    val members: Int,
    val popularity: Int,
    val published: Published,
    val publishing: Boolean,
    val rank: Int,
    val related: Related,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val score: Double,
    val scored_by: Int,
    val serializations: List<Serialization>,
    val status: String,
    val synopsis: String,
    val themes: List<Theme>,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val title_synonyms: List<Any>,
    val type: String,
    val url: String,
    val volumes: Int?
)