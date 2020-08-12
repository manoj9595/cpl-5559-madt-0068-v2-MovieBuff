package com.moviebuff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviebuff.R
import com.moviebuff.data.model.Movie
import kotlinx.android.synthetic.main.activity_reviews.*

class UpcomingMoviesAdapter : RecyclerView.Adapter<UpcomingMoviesAdapter.ViewHolder>() {

    private var mDataSet  = ArrayList<Movie>()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        val tvMovieName = itemView.findViewById<TextView>(R.id.tvMovieName)
        val tvReleaseDate = itemView.findViewById<TextView>(R.id.tvReleaseDate)
        val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        fun bind(movie: Movie) {

            Glide.with(ivPoster)
                .load(movie.getImageUrl())
                .into(ivPoster)

            tvMovieName.text = movie.original_title
            tvOverview.text = movie.overview
            tvReleaseDate.text = "Releasing on ${movie.release_date}"



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_upcoming,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDataSet[position])
    }

    fun setDataSet(it: java.util.ArrayList<Movie>) {
        mDataSet =  it
        notifyDataSetChanged()
    }
}