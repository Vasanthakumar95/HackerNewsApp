package com.example.hackernewsapp.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hackernewsapp.R
import com.example.hackernewsapp.model.comments.Hit
import kotlinx.android.synthetic.main.comments_view.view.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class CommentsPageAdapter : RecyclerView.Adapter<CommentsPageAdapter.CommentsViewHolder>(){

    inner class CommentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsPageAdapter.CommentsViewHolder {
        return CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.comments_view,
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
    override fun onBindViewHolder(holder: CommentsPageAdapter.CommentsViewHolder, position: Int) {
        val comments = differ.currentList[position]
        holder.itemView.apply {
            comment_author.text = getTimeDiff(comments.created_at) + comments?.author
            comment.text = comments?.comment_text.toString()
            num_comments.text = comments?.num_comments.toString() + "Comments"

            setOnClickListener {
                onItemClickListener?.let { it(comments) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Hit) -> Unit){
        onItemClickListener = listener
    }

//    Duration diff = Duration.between(instant2, instant1);
//    System.out.println(diff);

//    @RequiresApi(Build.VERSION_CODES.O)
//    var timedifference: Duration = Duration.between(LocalDateTime.now(), )
//
//    fun getCurrentDate():String{
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//        return sdf.format(Date())
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeDiff(timetocompare: String) : String?{

        val date1 = ZonedDateTime.now()
        val date2 = ZonedDateTime.parse(timetocompare)

        val diff = Duration.between(date2, date1)
        val hours = diff.toHours()
        diff.minusHours(hours)
        val minutes = diff.toMinutes()
        diff.minusMinutes(minutes)
        val seconds = diff.toMillis()

        if(hours.toInt() == 0)
        {
            return minutes.toString() + "m - "
        }
        else if(minutes.toInt() == 0)
        {
            return seconds.toString() + "s - "
        }else
            return hours.toString() + "h - "

    }

}