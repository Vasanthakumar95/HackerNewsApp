package com.example.hackernewsapp.model.frontpage

import java.io.Serializable

data class Title(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
) : Serializable