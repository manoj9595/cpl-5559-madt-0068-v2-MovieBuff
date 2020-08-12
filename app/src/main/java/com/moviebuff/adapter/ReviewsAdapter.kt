package com.moviebuff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviebuff.R
import com.moviebuff.data.model.Review
import com.moviebuff.data.model.User
import com.moviebuff.db.model.UserAndReview
import kotlinx.android.synthetic.main.nav_header_main.*

class ReviewsAdapter(val id: Int?) :RecyclerView.Adapter<ReviewsAdapter.ViewHolder>(){

    var mDataSet = ArrayList<UserAndReview>()
    lateinit var listener : ReviewsAction

    fun setDataSet(it: java.util.ArrayList<UserAndReview>) {
        mDataSet = it
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivMain = itemView.findViewById<ImageView>(R.id.ivMain)
        val tvReview = itemView.findViewById<TextView>(R.id.tvReview)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val ivDeleteReview = itemView.findViewById<ImageView>(R.id.ivDeleteReview)
        fun bind(review: Review,user :User) {

            tvReview.text = review.review
            tvUserName.text = user.firstName

            ivDeleteReview.visibility = if(review.userId==id) View.VISIBLE else View.GONE

            Glide.with(ivMain)
                .load(user.profilePic)
                .into(ivMain)

            ivDeleteReview.setOnClickListener {
                listener.onDeleteClicked(review)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_review,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDataSet[position].review,mDataSet[position].user)
    }
}

interface ReviewsAction{
    fun onDeleteClicked(review: Review)
}