package com.eros.moviesdb.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eros.moviesdb.R
import com.eros.moviesdb.adapter.FavouriteMovieAdapter
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity
import com.eros.moviesdb.model.response.Movies
import com.eros.moviesdb.model.response.TopRatedMoviesRes
import com.eros.moviesdb.utils.Constants
import com.eros.moviesdb.view.activities.BaseActivity
import com.eros.moviesdb.view.activities.MovieDetailActivity
import com.eros.moviesdb.viewholder.FavouriteMovieViewHolder
import com.eros.moviesdb.viewmodel.MainActivityViewModel

class FavouritesFragment : BaseFragment(), FavouriteMovieViewHolder.OnFavouriteMovieClickListener {

    private lateinit var viewModel : MainActivityViewModel
    private lateinit var tvNoFavMovie : TextView
    private lateinit var layFavMovies : View
    private lateinit var rvMovies : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager

    private lateinit var adapter : FavouriteMovieAdapter
    private var moviesLst = ArrayList<FavouriteMoviesEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseView = inflater.inflate(R.layout.layout_favourite_movies, container, false);
        return baseView
    }

    override fun initViews() {
        tvNoFavMovie = baseView.findViewById(R.id.tv_no_fav_movie)
        layFavMovies = baseView.findViewById(R.id.layout_fav_movies)
        rvMovies = layFavMovies.findViewById(R.id.rv_top_rated_movies)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        gridLayoutManager = GridLayoutManager(requireContext(), 3);
        rvMovies.layoutManager = gridLayoutManager;

        adapter = FavouriteMovieAdapter(requireContext())
        adapter.setClickListener(this)
        adapter.setData(moviesLst)
        rvMovies.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel::class.java)

        viewModel.showProgressDialog().observe(this, object : Observer<Boolean> {
            override fun onChanged(status: Boolean) {
                if (status) {
                    (requireActivity() as BaseActivity).showProgressDialog()
                } else {
                    (requireActivity() as BaseActivity).dismissProgressDialog()
                }
            }

        })

        viewModel.showError().observe(this, object : Observer<String> {
            override fun onChanged(errorMesg: String) {
                (requireActivity() as BaseActivity).showDialog("Error", errorMesg)
            }

        })

        viewModel.getAllFavouriteMovies().observe(this, object : Observer<ArrayList<FavouriteMoviesEntity>> {
            override fun onChanged(arrLst: ArrayList<FavouriteMoviesEntity>?) {
                if (arrLst != null && arrLst.size > 0) {
                    layFavMovies.visibility = View.VISIBLE
                    tvNoFavMovie.visibility = View.GONE

                    moviesLst.clear()
                    moviesLst.addAll(arrLst)
                    adapter.setData(moviesLst)
                    adapter.notifyDataSetChanged()
                } else {
                    tvNoFavMovie.visibility = View.VISIBLE
                    layFavMovies.visibility = View.GONE
                }
            }
        })
    }

    fun callMovieDetailScreen(movieObj: FavouriteMoviesEntity) {
        val intent = Intent(requireContext(), MovieDetailActivity::class.java)
        intent.putExtra(Constants.INTENT_MOVIE_ID, movieObj.movieId)
        requireContext().startActivity(intent)
    }

    override fun onItemClicked(movieObj: FavouriteMoviesEntity) {
        callMovieDetailScreen(movieObj)
    }
}