package com.example.hackernewsapp.model.comments

data class StoryTitle(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
)