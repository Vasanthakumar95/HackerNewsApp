package com.example.hackernewsapp.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hackernewsapp.R
import com.example.hackernewsapp.model.frontpage.Hit
import kotlinx.android.synthetic.main.news_preview.view.*
import java.time.Duration
import java.time.ZonedDateTime

class NewsPageAdapter : RecyclerView.Adapter<NewsPageAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    /**
     * diffutil is used to compare the list of old items and new items and only update the new ones.
     * and this does not block the main thread as it runs in the background
     */

    private val differCallback = object : DiffUtil.ItemCallback<Hit>()
    {
        override fun areItemsTheSame(
            oldItem: Hit,
            newItem: Hit
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Hit,
            newItem: Hit
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.news_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Hit) -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = differ.currentList[position]
        holder.itemView.apply {
            position_news.text = (position + 1).toString()
            title_news.text = news.title
            hits_news.text = (news.points).toString() + "p"
            url_news.text = news.url
            author_news.text = getTimeDiff(news.created_at) + news.author
            comment_count_news.text = news.num_comments.toString()

            setOnClickListener {
                onItemClickListener?.let { it(news) }
            }

        }
    }

    fun setOnItemClickListener(listener: (Hit) -> Unit){
        onItemClickListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeDiff(timetocompare: String) : String?{

        val date1 = ZonedDateTime.now()
        val date2 = ZonedDateTime.parse(timetocompare)

        val diff = Duration.between(date2, date1)
        val days = diff.toDays()
        diff.minusDays(days)
        val hours = diff.toHours()
        diff.minusHours(hours)
        val minutes = diff.toMinutes()
        diff.minusMinutes(minutes)
        val seconds = diff.toMillis()

        if(days.toInt() == 0){
            return hours.toString() + "h - "
        }
        else if(hours.toInt() == 0)
        {
            return minutes.toString() + "m - "
        }
        else if(minutes.toInt() == 0)
        {
            return seconds.toString() + "s - "
        }else
            return hours.toString() + "d - "

    }
}