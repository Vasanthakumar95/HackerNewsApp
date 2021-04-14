package com.example.hackernewsapp.model.frontpage

import java.io.Serializable

data class HighlightResult(
    val author: Author,
    val title: Title,
    val url: Url
) : Serializable