package com.example.hackernewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hackernewsapp.R
import com.example.hackernewsapp.adapters.ViewPagerAdapter
import com.example.hackernewsapp.ui.CoreActivity
import com.example.hackernewsapp.ui.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment(R.layout.fragment_info) {

    lateinit var viewModel: NewsViewModel

    private val args: NewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()

        viewModel = (activity as CoreActivity).viewModel

        val url = args.hit.url

        setStoryID(args.hit._tags[2])
        setArticleURL(args.hit.url)

        title.text = args.hit.title
        website.text = url
        lastupdate_author.text = args.hit.author

        back_button?.setOnClickListener {
            findNavController().navigate(R.id.action_infoFragment_to_newsFragment)

        }
    }

    private fun setUpTabs()
    {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(CommentsFragment(),  "${args.hit.num_comments} Comments")
        adapter.addFragment(ArticleFragment(), "Article")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }

    private fun setStoryID(story_id: String)
    {
        val PREFERENCE_NAME="story_id"
        val preference = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preference!!.edit()
        editor!!.putString(PREFERENCE_NAME, story_id)
        editor.apply()
    }

    private fun setArticleURL(url: String)
    {
        val PREFERENCE_NAME="url"
        val preference = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preference!!.edit()
        editor!!.putString(PREFERENCE_NAME, url)
        editor.apply()
    }

}