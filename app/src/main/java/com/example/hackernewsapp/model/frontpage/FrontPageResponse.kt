package com.example.hackernewsapp.model.frontpage

import java.io.Serializable

data class FrontPageResponse(
    val exhaustiveNbHits: Boolean,
    val hits: MutableList<Hit>,
    val hitsPerPage: Int,
    val nbHits: Int,
    val nbPages: Int,
    val page: Int,
    val params: String,
    val processingTimeMS: Int,
    val query: String
) : Serializable