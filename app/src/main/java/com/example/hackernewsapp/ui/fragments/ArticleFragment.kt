package com.example.hackernewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.hackernewsapp.R
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment : Fragment(R.layout.fragment_article) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        load_url(getArticleURL().toString())
    }

    private fun getArticleURL() : String?{
        val PREFERENCE_NAME="url"
        val preference = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preference?.getString(PREFERENCE_NAME, "")
    }

    private fun load_url(url : String)
    {
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }
}