package com.example.hackernewsapp.model.comments

data class CommentText(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
)