package com.moviebuff.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviebuff.R
import com.moviebuff.adapter.CastListAdapter
import com.moviebuff.adapter.UpcomingMoviesAdapter
import com.moviebuff.data.network.MyApi
import com.moviebuff.data.network.NetworkConnectionInterceptor
import com.moviebuff.data.repositories.MovieRepository
import com.moviebuff.utils.Coroutines
import com.moviebuff.utils.setGone
import com.moviebuff.utils.setVisible
import com.moviebuff.utils.toast
import kotlinx.android.synthetic.main.activity_upcoming_movies.*
import java.lang.Exception

class UpcomingMoviesActivity : AppCompatActivity() {
    lateinit var adapter : UpcomingMoviesAdapter
    lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movies)
        repository = MovieRepository(MyApi(NetworkConnectionInterceptor()))
        setToolbar()
        setRecycler()
        getUpComingMovies()
    }
    private fun setRecycler() {
        rvUpcomingMovies.layoutManager = LinearLayoutManager(this)
        adapter = UpcomingMoviesAdapter()
        rvUpcomingMovies.adapter = adapter
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Upcoming Movies"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    private fun getUpComingMovies(){
        progressBar.setVisible()
        Coroutines.io {
            try{
                val  response = repository.getUpComingMovies()
                Coroutines.main {
                    progressBar.setGone()
                    response.results?.let {
                        adapter.setDataSet(it)
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