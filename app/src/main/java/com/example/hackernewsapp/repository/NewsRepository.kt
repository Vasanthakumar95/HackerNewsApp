package com.example.hackernewsapp.repository

import com.example.hackernewsapp.API.RetrofitInstance

class NewsRepository(
) {

    suspend fun getFrontPageNews(tags: String, hitsPerPage: Int, page: Int) =
        RetrofitInstance.api.getFrontPage(tags,hitsPerPage,page)

    suspend fun getSelectedNewsComments(tags: String, hitsPerPage: Int, page: Int) =
        RetrofitInstance.api.getStoryComments(tags,hitsPerPage,page)
}