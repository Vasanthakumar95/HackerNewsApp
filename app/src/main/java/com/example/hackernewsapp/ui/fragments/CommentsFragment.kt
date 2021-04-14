package com.example.hackernewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackernewsapp.R
import com.example.hackernewsapp.adapters.CommentsPageAdapter
import com.example.hackernewsapp.ui.CoreActivity
import com.example.hackernewsapp.ui.viewmodels.NewsViewModel
import com.example.hackernewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_news.paginationProgressBar


class CommentsFragment : Fragment(R.layout.fragment_comments) {


    val TAG = "NewsComments"

    private lateinit var viewModel: NewsViewModel
    private lateinit var commentspageAdapter: CommentsPageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as CoreActivity).viewModel

        viewModel.getSelectedNewsComments("comment,${getStoryID()}")

        setupRecyclerView()

        viewModel.selectedNewsCommentsLiveData
            .observe(viewLifecycleOwner, Observer { response ->
            when(response)
            {
                is Resource.Success ->
                {
                    hideProgressBar()
                    response.data?.let { hitResponse ->
                        commentspageAdapter.differ.submitList(hitResponse.hits.toList())
                        val totalPages = hitResponse.nbPages
                        isLastPage = viewModel.comment_page == totalPages
                        if(isLastPage){
                            rvNewsComments.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, message)
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->
                {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener()
    {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 25
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getSelectedNewsComments("comment,${getStoryID()}")
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView()
    {
        commentspageAdapter = CommentsPageAdapter()
        rvNewsComments.apply {
            adapter = commentspageAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@CommentsFragment.scrollListener)
        }
    }

    private fun getStoryID() : String?{
        val PREFERENCE_NAME="story_id"
        val preference = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        Toast.makeText(activity, preference!!.getString(PREFERENCE_NAME, ""), Toast.LENGTH_SHORT).show()
        return preference!!.getString(PREFERENCE_NAME, "")
    }

}