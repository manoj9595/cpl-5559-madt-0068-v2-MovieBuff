package com.moviebuff

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.moviebuff.adapter.HorizontalPagerAdapter
import com.moviebuff.data.model.Movie
import com.moviebuff.data.model.User
import com.moviebuff.data.network.MyApi
import com.moviebuff.data.network.NetworkConnectionInterceptor
import com.moviebuff.data.repositories.AppPreferencesHelper
import com.moviebuff.data.repositories.MovieRepository
import com.moviebuff.db.AppDataBase
import com.moviebuff.ui.home.CastAndCrewActivity
import com.moviebuff.ui.home.ReviewsActivity
import com.moviebuff.ui.home.UpcomingMoviesActivity
import com.moviebuff.ui.login.SignUpActivity
import com.moviebuff.utils.Coroutines
import com.moviebuff.utils.setGone
import com.moviebuff.utils.setVisible
import com.moviebuff.utils.toast
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {


    lateinit var repository: MovieRepository
    var mDataSet  = ArrayList<Movie>()
    lateinit var adapter: HorizontalPagerAdapter
    var player : YouTubePlayer?=null
    var currentPos : Int?=null
    lateinit var drawerLayout : DrawerLayout
    var userObject : User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        repository = MovieRepository(MyApi(NetworkConnectionInterceptor()))
        setUpVideoFrag()

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        initCovers()
        getMoviesList()

        setListeners()

    }

    override fun onResume() {
        super.onResume()
        initUserDetails()
    }

    private fun initUserDetails() {
        Coroutines.io {
            val user  = AppDataBase.getAppDataBase(this)?.userDao()?.getUserByEmailId(AppPreferencesHelper(this)
                .getSavedEmailId()!!)
            user?.let {
                Coroutines.main {
                    userObject  = it
                    tvName.text = "${it.firstName} ${it.lastName}"
                    tvMobileNumber.text = it.contact

                    Glide.with(this)
                        .load(it.profilePic)
                        .into(ivUserImage)


                }
                }
        }
    }

    private fun setListeners() {
        bCast.setOnClickListener {
            currentPos?.let {
                startActivity(Intent(this,CastAndCrewActivity::class.java).apply {
                    this.putExtra("movie",mDataSet[it])
                })
            }
        }
        bReviews.setOnClickListener {
            currentPos?.let {
                startActivity(Intent(this,ReviewsActivity::class.java).apply {
                    this.putExtra("movie",mDataSet[it])
                })
            }
        }

        tvLogout.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            AppPreferencesHelper(this).logoutUser()
        }

        bSynopsis.setOnClickListener {
            currentPos?.let {
                AlertDialog.Builder(this).apply {
                    this.setTitle(mDataSet[it].title)
                    this.setMessage(mDataSet[it].overview)
                }.create().show()
            }
        }
        rlProfile.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            userObject?.let {
                startActivity(Intent(this,SignUpActivity::class.java).apply {
                    this.putExtra("user",it)
                })
            }
        }
        tvUpcomingMovies.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this,UpcomingMoviesActivity::class.java))
        }
    }

    private fun setUpVideoFrag() {
        val youTubePlayerFragment =
            fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
        youTubePlayerFragment.initialize(getString(R.string.youtube_key), this)
    }

    private fun initCovers() {
        adapter = HorizontalPagerAdapter()
        hicvp.adapter = adapter


        hicvp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPos = position
                getVideo(mDataSet[position].id)
            }

        })
    }



    private fun getMoviesList(){
        progressBar.setVisible()
        Coroutines.io {
            try{
                val  response = repository.getTopRatedMovies()
                Coroutines.main {
                    progressBar.setGone()
                }
                response.results?.let {
                    mDataSet = it

                    Coroutines.main {
                        adapter.setDataSet(it)

                    }
                    if(it.size>0){
                        currentPos = 0
                        getVideo(it[0].id)
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

    private fun getVideo(movieId : Int){
        Coroutines.main {
            progressBar.setVisible()
        }
        Coroutines.io {
            try{
                val  response = repository.getVideo(movieId)

                Coroutines.main {
                    progressBar.setGone()
                }
                response.results?.let {

                    if(it.size>0){
                        Coroutines.main {
                            player?.cueVideo(it[0].key)
                        }
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


    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean) {
        if (!p2) {
            player = p1
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?) {}
}