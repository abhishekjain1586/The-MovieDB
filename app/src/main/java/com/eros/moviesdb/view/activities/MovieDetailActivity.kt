package com.eros.moviesdb.view.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.eros.moviesdb.R
import com.eros.moviesdb.model.response.Genre
import com.eros.moviesdb.model.response.MovieDetailRes
import com.eros.moviesdb.utils.Constants
import com.eros.moviesdb.viewmodel.MovieDetailActivityViewModel
import com.squareup.picasso.Picasso

class MovieDetailActivity : BaseActivity() {

    lateinit var viewModel : MovieDetailActivityViewModel
    lateinit var ivPoster : ImageView
    lateinit var tvOverview : TextView
    lateinit var tvMovieTitle : TextView
    lateinit var tvGenre : TextView
    lateinit var tvReleaseDate : TextView

    private var movieId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        setTitle(getString(R.string.movie_detail))
        movieId = intent.getIntExtra(Constants.INTENT_MOVIE_ID, -1)

        ivPoster = findViewById(R.id.iv_poster)
        tvOverview = findViewById(R.id.tv_overview)
        tvMovieTitle = findViewById(R.id.tv_title)
        tvGenre = findViewById(R.id.tv_genre)
        tvReleaseDate = findViewById(R.id.tv_release_date)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieDetailActivityViewModel::class.java)

        viewModel.showProgressDialog().observe(this, object : Observer<Boolean> {
            override fun onChanged(status: Boolean) {
                if (status) {
                    showProgressDialog()
                } else {
                    dismissProgressDialog()
                }
            }

        })

        viewModel.showError().observe(this, object : Observer<String> {
            override fun onChanged(errorMesg: String) {
                showDialog("Error", errorMesg)
            }

        })

        if (movieId != -1) {
            viewModel.getMovieDetail(movieId).observe(this, object : Observer<MovieDetailRes> {
                override fun onChanged(movieDetailRes : MovieDetailRes?) {
                    movieDetailRes?.let { setDataToUI(movieDetailRes) }
                }
            })
        }
    }

    fun setDataToUI(movieDetailRes : MovieDetailRes?) {
        movieDetailRes?.title?.let {
            tvMovieTitle.text = it
            setTitle(it)
        }

        movieDetailRes?.poster_path?.let {
            Picasso.with(this).load("https://image.tmdb.org/t/p/w"+Constants.POSTER_SIZE_500 + it)
                .resize(Constants.POSTER_SIZE_500, Constants.POSTER_SIZE_500)
                .centerInside()
                .error(ContextCompat.getDrawable(this, R.drawable.no_picture_256))
                .into(ivPoster);
        }

        movieDetailRes?.overview?.let {
            tvOverview.text = it
        }

        movieDetailRes?.release_date?.let {
            tvReleaseDate.text = it
        }

        var strGenre = movieDetailRes?.genreLst?.let {
            getGenre(it)
        }

        if (!strGenre.isNullOrEmpty()) {
            tvGenre.text = strGenre
        } else {
            tvGenre.text = "NA"
        }
    }

    fun getGenre(genreLst : ArrayList<Genre>) : String {
        var strGenre = Constants.EMPTY
        for (genreObj in genreLst) {
            if (strGenre.isEmpty()) {
                genreObj.name?.let { strGenre = it }
            } else {
                genreObj.name?.let { strGenre = strGenre + ", " + it }
            }
        }

        return strGenre
    }
}