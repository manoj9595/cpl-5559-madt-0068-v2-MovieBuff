package com.moviebuff.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.moviebuff.R
import com.moviebuff.adapter.ReviewsAction
import com.moviebuff.adapter.ReviewsAdapter
import com.moviebuff.data.model.Movie
import com.moviebuff.data.model.Review
import com.moviebuff.data.model.User
import com.moviebuff.data.repositories.AppPreferencesHelper
import com.moviebuff.db.AppDataBase
import com.moviebuff.db.model.UserAndReview
import com.moviebuff.utils.Coroutines
import com.moviebuff.utils.setGone
import com.moviebuff.utils.setVisible
import com.moviebuff.utils.toast
import kotlinx.android.synthetic.main.activity_reviews.*
import kotlinx.android.synthetic.main.activity_reviews.ivPoster
import kotlinx.android.synthetic.main.activity_reviews.toolbar
import kotlinx.android.synthetic.main.activity_reviews.tvMovieName
import kotlinx.android.synthetic.main.activity_reviews.tvMovieReleaseDate
import java.lang.Exception
import java.util.ArrayList

class ReviewsActivity : AppCompatActivity() {

    lateinit var adapter : ReviewsAdapter
    lateinit var user  :User
    var movieObject : Movie?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)
        setToolbar()
        movieObject = intent.getSerializableExtra("movie") as Movie

        initMovieData()
        initUser()
    }
    private fun initMovieData() {
        movieObject?.let {
            Glide.with(this)
                .load(it.getImageUrl())
                .into(ivPoster)

            tvMovieName.text = it.original_title
            tvMovieReleaseDate.text = "Release Date ${it.release_date}"
        }
    }

    private fun initUser() {
        Coroutines.io {
            val user  = AppDataBase.getAppDataBase(this)?.userDao()?.getUserByEmailId(AppPreferencesHelper(this)
                .getSavedEmailId()!!)
            if(user!=null){
                this.user = user
                Coroutines.main {
                    setRecycler()
                    fetchReviews()
                }
            }
        }
    }

    private fun setRecycler() {
        rvReviews.layoutManager = LinearLayoutManager(this)
        adapter = ReviewsAdapter(user.id)
        rvReviews.adapter = adapter


        adapter.listener = object  : ReviewsAction {
            override fun onDeleteClicked(review: Review) {
                Coroutines.io {
                    try{
                        AppDataBase.getAppDataBase(this@ReviewsActivity)?.reviewDao()?.deleteReview(review)
                        fetchReviews()
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Reviews"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    fun AddReview(view: View) {
        val enteredReview = etReview.text.toString().trim()
        if(enteredReview.isEmpty()) {
            toast("Enter Review")
            return
        }

        val review = Review(userId = user.id!!,
        movieId = movieObject?.id!!,
        review = enteredReview)

        Coroutines.io {
            try{
                AppDataBase.getAppDataBase(this)?.reviewDao()?.addReview(review)
                fetchReviews()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    private fun fetchReviews(){
        Coroutines.io {
            val reviews  = AppDataBase.getAppDataBase(this)?.reviewDao()?.getReviewsAndUsers(movieObject?.id!!)
            reviews?.let {
                Coroutines.main {
                    adapter.setDataSet(it as ArrayList<UserAndReview>)
                    if(checkUserReview(it)){
                        llAddReview.setGone()
                    }else{
                        llAddReview.setVisible()
                    }
                }
            }
        }
    }

    private fun checkUserReview(reviews :ArrayList<UserAndReview>) :Boolean{
        for(review in reviews){
            if(review.review.userId == user.id){
                return true
            }
        }
        return false
    }
}