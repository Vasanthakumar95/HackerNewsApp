package com.example.hackernewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.hackernewsapp.R
import com.example.hackernewsapp.repository.NewsRepository
import com.example.hackernewsapp.ui.ViewModelProviderFactory.NewsViewModelProviderFactory
import com.example.hackernewsapp.ui.viewmodels.NewsViewModel

class CoreActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_core)

        val newsRepository = NewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

    }

}