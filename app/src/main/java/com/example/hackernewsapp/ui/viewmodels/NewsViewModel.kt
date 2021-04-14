package com.example.hackernewsapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hackernewsapp.NewsApplication
import com.example.hackernewsapp.model.comments.ArticleComments
import com.example.hackernewsapp.model.frontpage.FrontPageResponse
import com.example.hackernewsapp.repository.NewsRepository
import com.example.hackernewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    val newsRepository : NewsRepository
) : AndroidViewModel(app) {

    val frontPageLiveData: MutableLiveData<Resource<FrontPageResponse>> = MutableLiveData()
    //current page of loading front page news
    var frontNews_Page = 0
    var frontpageResponse: FrontPageResponse? = null

    val selectedNewsCommentsLiveData: MutableLiveData<Resource<ArticleComments>> = MutableLiveData()
    var comment_page = 0

    init {

        getFrontPageNews("front_page")

    }

    fun getFrontPageNews(tags: String) = viewModelScope.launch {
        safeBreakFrontPageCall(tags)
    }

    fun handleFrontPageNewsResponse(response: Response<FrontPageResponse>) : Resource<FrontPageResponse>
    {
        if(response.isSuccessful)
        {
            response.body()?.let { resultResponse ->
                frontNews_Page++
                if (frontpageResponse == null)
                {
                    frontpageResponse = resultResponse
                } else {
                    val oldpageresponse = frontpageResponse?.hits
                    val newpageresponse = resultResponse.hits
                    oldpageresponse?.addAll(newpageresponse)
                }
                return Resource.Success(frontpageResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun getSelectedNewsComments(tags: String) = viewModelScope.launch {
        safeBreakNewsCommentCall(tags)
    }

    fun handleSelectedNewsCommentsResponse(response: Response<ArticleComments>) : Resource<ArticleComments>
    {
        if(response.isSuccessful)
        {
            response.body()?.let { resultCommentResponse ->
                return Resource.Success(resultCommentResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakFrontPageCall(tags: String)
    {
        frontPageLiveData.postValue(Resource.Loading())
        try {
            if (internetConnectionChecker())
            {
                val response = newsRepository.getFrontPageNews(tags, 25 , frontNews_Page)
                frontPageLiveData.postValue(handleFrontPageNewsResponse(response))
            }else
            {
                frontPageLiveData.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable)
        {
            when(t)
            {
                is IOException -> frontPageLiveData.postValue(Resource.Error("Network Failure"))
                else -> frontPageLiveData.postValue(Resource.Error("${t.message}"))
            }
        }
    }

    private suspend fun safeBreakNewsCommentCall(tags: String)
    {
        selectedNewsCommentsLiveData.postValue(Resource.Loading())
        try {
            if (internetConnectionChecker())
            {
                val response = newsRepository.getSelectedNewsComments(tags, 200, comment_page)
                selectedNewsCommentsLiveData.postValue(handleSelectedNewsCommentsResponse(response))
            }else
            {
                selectedNewsCommentsLiveData.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable)
        {
            when(t)
            {
                is IOException -> selectedNewsCommentsLiveData.postValue(Resource.Error("Network Failure"))
                else -> selectedNewsCommentsLiveData.postValue(Resource.Error("${t.message}"))
            }
        }
    }

    private fun internetConnectionChecker(): Boolean
    {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type)
                {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}