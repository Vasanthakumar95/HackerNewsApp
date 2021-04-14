package com.example.hackernewsapp.API

import com.example.hackernewsapp.model.comments.ArticleComments
import com.example.hackernewsapp.model.frontpage.FrontPageResponse
import com.example.hackernewsapp.model.frontpage.Hit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HackerNewsAPI {

    /**
     * no API key is needed as of now since its an open source
     */


    //tags=front_page&hitsPerPage=25&page=1 (main parameters for the request)
    @GET("v1/search_by_date?")
    suspend fun getFrontPage(
        @Query("tags")
        tags: String = "front_page",
        @Query("hitsPerPage")
        hitsPerPage: Int = 25,
        @Query("page")
        page: Int = 0
    ): Response<FrontPageResponse>

    /**
     * make the parameters dynamic later on
     */

    //tags=front_page&hitsPerPage=25&page=1 (main parameters for the request)
    @GET("v1/search_by_date?")
    suspend fun getStoryComments(
        @Query("tags")
        tags: String = "comment,story_26699106",
        @Query("hitsPerPage")
        hitsPerPage: Int = 25,
        @Query("page")
        page: Int = 0

    ): Response<ArticleComments>


}