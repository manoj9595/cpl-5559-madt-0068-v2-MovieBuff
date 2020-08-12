package com.moviebuff.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.moviebuff.R
import com.moviebuff.adapter.CastListAdapter
import com.moviebuff.data.model.Movie
import com.moviebuff.data.network.MyApi
import com.moviebuff.data.network.NetworkConnectionInterceptor
import com.moviebuff.data.repositories.MovieRepository
import com.moviebuff.utils.Coroutines
import com.moviebuff.utils.setGone
import com.moviebuff.utils.setVisible
import com.moviebuff.utils.toast
import kotlinx.android.synthetic.main.activity_cast_and_crew.*
import java.lang.Exception

class CastAndCrewActivity : AppCompatActivity() {
    lateinit var repository: MovieRepository

    var movieObject : Movie?=null
    lateinit var adapter : CastListAdapter
    lateinit var crewAdapter : CastListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_and_crew)
        repository = MovieRepository(MyApi(NetworkConnectionInterceptor()))
        setToolbar()
        movieObject = intent.getSerializableExtra("movie") as Movie

        initMovieData()

        setRecycler()
        getCastList(movieObject!!.id)
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

    private fun setRecycler() {
        rvCast.layoutManager = LinearLayoutManager(this)
        adapter = CastListAdapter()
        rvCast.adapter = adapter

        rvCrew.layoutManager = LinearLayoutManager(this)
        crewAdapter = CastListAdapter()
        rvCrew.adapter = crewAdapter
    }
    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Cast & Crew"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }


    private fun getCastList(movieId  :Int){

        progressBar.setVisible()
        Coroutines.io {
            try{
                val  response = repository.getCastList(movieId)
                Coroutines.main {
                    progressBar.setGone()
                    response.cast?.let {
                        adapter.setDataSet(it)
                    }

                    response.crew?.let {
                        crewAdapter.setDataSet(it)
                    }
                }
            }catch (e : Exception){
                Coroutines.main {
                    progressBar.setGone()
                    toast("Something went Wrong")
                }
            }

        }
    }
}