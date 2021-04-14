package com.example.hackernewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackernewsapp.R
import com.example.hackernewsapp.adapters.NewsPageAdapter
import com.example.hackernewsapp.ui.CoreActivity
import com.example.hackernewsapp.ui.viewmodels.NewsViewModel
import com.example.hackernewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment(R.layout.fragment_news) {

    val TAG = "FrontPageNews"

    private lateinit var viewModel: NewsViewModel
    private lateinit var newspageAdapter: NewsPageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as CoreActivity).viewModel
        setupRecyclerView()

        newspageAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("hit", it)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_infoFragment,
                bundle
            )
            Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show()
        }

        viewModel.frontPageLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response)
            {
                is Resource.Success ->
                {
                    hideProgressBar()
                    response.data?.let { hitResponse ->
                        newspageAdapter.differ.submitList(hitResponse.hits)
                        val totalPages = hitResponse.nbPages
                        isLastPage = viewModel.frontNews_Page == totalPages
                        if(isLastPage){
                            rvFrontPageNews.setPadding(0, 0, 0, 0)
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
                viewModel.getFrontPageNews("front_page")
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
        newspageAdapter = NewsPageAdapter()
        rvFrontPageNews.apply {
            adapter = newspageAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.scrollListener)
        }
    }

}