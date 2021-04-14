package com.example.hackernewsapp.model.comments

data class HighlightResult(
    val author: Author,
    val comment_text: CommentText,
    val story_title: StoryTitle,
    val story_url: StoryUrl
)