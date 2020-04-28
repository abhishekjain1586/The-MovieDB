package com.eros.moviesdb.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eros.moviesdb.R
import com.eros.moviesdb.adapter.TopRatedMoviesAdapter
import com.eros.moviesdb.db.DBHelper
import com.eros.moviesdb.model.response.Movies
import com.eros.moviesdb.model.response.TopRatedMoviesRes
import com.eros.moviesdb.utils.Constants
import com.eros.moviesdb.view.activities.BaseActivity
import com.eros.moviesdb.view.activities.MovieDetailActivity
import com.eros.moviesdb.viewholder.TopRatedMoviesViewHolder
import com.eros.moviesdb.viewmodel.MainActivityViewModel

class TopRatedMoviesFragment : BaseFragment(), TopRatedMoviesViewHolder.OnTopRatedMoviesClickListener {

    private lateinit var viewModel : MainActivityViewModel
    private lateinit var rvMovies : RecyclerView
    private lateinit var rvSearchedMovies : RecyclerView
    private lateinit var searchLayout : ConstraintLayout
    private lateinit var gridLayoutManager : GridLayoutManager
    private lateinit var linearLayoutManager : LinearLayoutManager

    private var pageNoTopRatedMovies = 1
    private lateinit var adapterTopRatedMovies : TopRatedMoviesAdapter
    private var resTopRatedMovies : TopRatedMoviesRes? = null
    private var topRatedMoviesLst = ArrayList<Movies>()

    private var pageNoSearchedMovies = 1
    private lateinit var adapterSearchedMovies : TopRatedMoviesAdapter
    private var searchedMoviesLst = ArrayList<Movies>()

    private val TOP_RATED_HANDLER_CODE = 1
    private val SEARCHED_HANDLER_CODE = 2
    private var isTopRatedMoviesLoadingMore = false
    private var isSearchedMoviesLoadingMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseView = inflater.inflate(R.layout.layout_top_rated_movies, container, false);
        return baseView
    }

    override fun initViews() {
        val layTopRatedMovies = baseView.findViewById(R.id.layout_top_rated_movies) as View
        rvMovies = layTopRatedMovies.findViewById(R.id.rv_top_rated_movies)

        val laySearchedMovies = baseView.findViewById(R.id.layout_searched_movies) as View
        rvSearchedMovies = laySearchedMovies.findViewById(R.id.rv_top_rated_movies)
        searchLayout = baseView.findViewById(R.id.layout1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        // Adapter for Top Rated Movies
        gridLayoutManager = GridLayoutManager(requireContext(), 3);
        rvMovies.layoutManager = gridLayoutManager;

        adapterTopRatedMovies = TopRatedMoviesAdapter(requireContext())
        adapterTopRatedMovies.setClickListener(this)
        adapterTopRatedMovies.setData(topRatedMoviesLst)
        rvMovies.adapter = adapterTopRatedMovies
        /* */

        // Adapter for Searched Movies
        linearLayoutManager = LinearLayoutManager(requireContext());
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvSearchedMovies.layoutManager = linearLayoutManager;

        adapterSearchedMovies = TopRatedMoviesAdapter(requireContext(), false)
        adapterSearchedMovies.setClickListener(this)
        adapterSearchedMovies.setData(searchedMoviesLst)
        rvSearchedMovies.adapter = adapterSearchedMovies
        /* */

        initListener()
    }

    private fun initListener() {
        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount: Int = gridLayoutManager.childCount
                val totalItemCount: Int = gridLayoutManager.itemCount
                val firstVisibleItemPosition: Int = gridLayoutManager.findFirstVisibleItemPosition()

                if (isTopRatedMoviesLoadingMore) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        isTopRatedMoviesLoadingMore = false;
                        resTopRatedMovies?.let { viewModel.loadMoreTopRatedMovies(++pageNoTopRatedMovies) }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        rvSearchedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()

                if (isSearchedMoviesLoadingMore) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        isSearchedMoviesLoadingMore = false;
                        viewModel.loadMoreSearchedMovies(++pageNoSearchedMovies)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
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

        viewModel.getTopRatedMovies().observe(this, object : Observer<TopRatedMoviesRes> {
            override fun onChanged(response: TopRatedMoviesRes?) {
                resTopRatedMovies = response

                isTopRatedMoviesLoadingMore = false
                resTopRatedMovies?.total_pages?.let {
                    if (pageNoTopRatedMovies < it) {
                        isTopRatedMoviesLoadingMore = true
                    }
                }

                resTopRatedMovies?.moviesLst?.let {
                    topRatedMoviesLst.addAll(it)
                    refreshTopRatedMoviesData(null)
                }
            }
        })

        viewModel.getSearchedMovies().observe(this, object : Observer<TopRatedMoviesRes> {
            override fun onChanged(searchedMovieRes: TopRatedMoviesRes?) {
                isSearchedMoviesLoadingMore = false
                searchedMovieRes?.total_pages?.let {
                    if (pageNoSearchedMovies < it) {
                        isSearchedMoviesLoadingMore = true
                    } else {
                        pageNoSearchedMovies = it
                    }
                }

                searchedMovieRes?.moviesLst?.let { setSearchedMoviesData(it) }
            }
        })

        viewModel.resetOldSearchedMovies().observe(this, object : Observer<Boolean> {
            override fun onChanged(status: Boolean?) {
                searchedMoviesLst.clear()
                adapterSearchedMovies.setData(searchedMoviesLst)
                adapterSearchedMovies.notifyDataSetChanged()
            }
        })

        viewModel.removeSearchView().observe(this, object : Observer<Boolean> {
            override fun onChanged(status: Boolean?) {
                searchedMoviesLst.clear()
                adapterSearchedMovies.setData(searchedMoviesLst)
                adapterSearchedMovies.notifyDataSetChanged()
                searchLayout.visibility = View.GONE
            }
        })
    }

    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message?) {
            when(msg?.what) {
                TOP_RATED_HANDLER_CODE -> {
                    adapterTopRatedMovies.setData(topRatedMoviesLst)
                    adapterTopRatedMovies.notifyDataSetChanged()
                }
                SEARCHED_HANDLER_CODE -> {
                    adapterSearchedMovies.setData(searchedMoviesLst)
                    adapterSearchedMovies.notifyDataSetChanged()
                }
            }
        }
    }

    fun setSearchedMoviesData(moviesLst : ArrayList<Movies>) {

        if (moviesLst.isNotEmpty()) {
            searchLayout.visibility = View.VISIBLE
            searchedMoviesLst.addAll(moviesLst)
            refreshSearchedMoviesData(null)
        } else {
            searchLayout.visibility = View.GONE
            Toast.makeText(requireContext(), requireContext().getString(R.string.no_search_result), Toast.LENGTH_LONG).show()
        }
    }

    private fun refreshTopRatedMoviesData(obj: Movies?) {
        Thread(object : Runnable {
            override fun run() {
                if (obj == null) {
                    val favMoviesLst = DBHelper.getInstance().getFavouriteMoviesDao().getAllFavouriteMovies()
                    if (favMoviesLst != null && favMoviesLst.isNotEmpty()) {
                        for (favMovieObj in favMoviesLst) {
                            for (movieObj in topRatedMoviesLst) {
                                if (favMovieObj.movieId == movieObj.id) {
                                    movieObj.isFavourite = true
                                }
                            }
                        }
                    }
                } else {
                    for (movieObj in topRatedMoviesLst) {
                        if (obj.id == movieObj.id) {
                            movieObj.isFavourite = obj.isFavourite
                        }
                    }
                }
                mHandler.sendEmptyMessage(TOP_RATED_HANDLER_CODE)
            }
        }).start()
    }

    private fun refreshSearchedMoviesData(obj: Movies?) {
        Thread(object : Runnable {
            override fun run() {
                if (obj == null) {
                    val favMoviesLst = DBHelper.getInstance().getFavouriteMoviesDao().getAllFavouriteMovies()
                    if (favMoviesLst != null && favMoviesLst.isNotEmpty()) {
                        for (favMovieObj in favMoviesLst) {
                            for (movieObj in searchedMoviesLst) {
                                if (favMovieObj.movieId == movieObj.id) {
                                    movieObj.isFavourite = true
                                }
                            }
                        }
                    }
                } else {
                    for (movieObj in searchedMoviesLst) {
                        if (obj.id == movieObj.id) {
                            movieObj.isFavourite = obj.isFavourite
                        }
                    }
                }
                mHandler.sendEmptyMessage(SEARCHED_HANDLER_CODE)
            }
        }).start()
    }


    override fun onItemClicked(movieObj: Movies) {
        callMovieDetailScreen(movieObj)
    }

    override fun onFavouritesClicked(movieObj: Movies) {
        if (!movieObj.isFavourite) {
            movieObj.isFavourite = true
            viewModel.addFavouriteMovieToDb(movieObj)
            Toast.makeText(requireContext(), requireContext().resources.getString(R.string.added_to_favourites), Toast.LENGTH_LONG).show()
        } else {
            movieObj.isFavourite = false
            viewModel.removeFavouriteMovieFromDb(movieObj)
            Toast.makeText(requireContext(), requireContext().resources.getString(R.string.removed_from_favourites), Toast.LENGTH_LONG).show()
        }

        refreshTopRatedMoviesData(movieObj)
        refreshSearchedMoviesData(movieObj)

    }

    fun callMovieDetailScreen(movieObj: Movies) {
        val intent = Intent(requireContext(), MovieDetailActivity::class.java)
        intent.putExtra(Constants.INTENT_MOVIE_ID, movieObj.id)
        requireContext().startActivity(intent)
    }

}