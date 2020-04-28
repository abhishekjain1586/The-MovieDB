package com.eros.moviesdb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eros.moviesdb.db.DBHelper
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity
import com.eros.moviesdb.model.response.Movies
import com.eros.moviesdb.model.response.TopRatedMoviesRes
import com.eros.moviesdb.repository.FavouriteMoviesRepository
import com.eros.moviesdb.repository.SearchedMoviesRepository
import com.eros.moviesdb.repository.TopRatedMoviesRepository
import com.eros.moviesdb.utils.Constants

class MainActivityViewModel : ViewModel(), TopRatedMoviesRepository.OnTopRatedMoviesListener,
    SearchedMoviesRepository.OnSearchedMoviesListener {

    private var repoTopRatedMovies = TopRatedMoviesRepository()
    private var repoSearchedMovies = SearchedMoviesRepository()
    private var repoFavouriteMovies = FavouriteMoviesRepository()

    private lateinit var topRatedMoviesLiveData : MutableLiveData<TopRatedMoviesRes>
    private lateinit var searchedMoviesLiveData : MutableLiveData<TopRatedMoviesRes>
    private lateinit var resetOldSearchedMoviesLiveData : MutableLiveData<Boolean>
    private lateinit var removeSearchedMoviesLiveData : MutableLiveData<Boolean>
    private lateinit var favouriteMoviesLiveData : MutableLiveData<ArrayList<FavouriteMoviesEntity>>
    private lateinit var loader : MutableLiveData<Boolean>
    private lateinit var displayMessage : MutableLiveData<String>
    private var searchedQuery = Constants.EMPTY

    fun getTopRatedMovies() : MutableLiveData<TopRatedMoviesRes> {
        if (!::topRatedMoviesLiveData.isInitialized) {
            topRatedMoviesLiveData = MutableLiveData<TopRatedMoviesRes>()
            repoTopRatedMovies.setListener(this)
        }
        getTopRatedMovFromRepo(1)
        return topRatedMoviesLiveData
    }

    fun loadMoreTopRatedMovies(page : Int) {
        getTopRatedMovFromRepo(page)
    }

    private fun getTopRatedMovFromRepo(page : Int) {
        loader.value = true
        repoTopRatedMovies.getTopRatedMovies(page)
    }

    override fun onTopRatedMoviesSuccess(topRatedMoviesRes: TopRatedMoviesRes) {
        loader.value = false
        topRatedMoviesLiveData.value = topRatedMoviesRes
    }

    override fun onTopRatedMoviesFailure(errorMsg: String) {
        loader.value = false
        displayMessage.value = errorMsg
    }


    fun loadMoreSearchedMovies(page : Int) {
        setSearchQuery(page, searchedQuery)
    }

    fun setSearchQuery(page : Int, query : String) {
        loader.value = true
        if (!query.equals(searchedQuery, true)) {
            resetOldSearchedMoviesLiveData.value = true
        }
        searchedQuery = query
        repoSearchedMovies.getSearchedMovies(page, query)
    }

    fun resetOldSearchedMovies() : MutableLiveData<Boolean> {
        if (!::resetOldSearchedMoviesLiveData.isInitialized) {
            resetOldSearchedMoviesLiveData = MutableLiveData<Boolean>()
        }
        return resetOldSearchedMoviesLiveData
    }

    fun removeSearchView() : MutableLiveData<Boolean> {
        if (!::removeSearchedMoviesLiveData.isInitialized) {
            removeSearchedMoviesLiveData = MutableLiveData<Boolean>()
        }
        return removeSearchedMoviesLiveData
    }

    fun removeSearchResults() {
        removeSearchedMoviesLiveData.value = true
    }

    fun getSearchedMovies() : MutableLiveData<TopRatedMoviesRes> {
        if (!::searchedMoviesLiveData.isInitialized) {
            searchedMoviesLiveData = MutableLiveData<TopRatedMoviesRes>()
            repoSearchedMovies.setListener(this)
        }
        return searchedMoviesLiveData
    }

    override fun onSearchedMoviesSuccess(topRatedMoviesRes: TopRatedMoviesRes) {
        loader.value = false
        /*topRatedMoviesRes.total_pages?.let {
            toLoadMoreSearchedMovies = pageNoSearchedMovies < it
        }*/
        searchedMoviesLiveData.value = topRatedMoviesRes
    }

    override fun onSearchedMoviesFailure(errorMsg: String) {
        loader.value = false
        displayMessage.value = errorMsg
    }


    fun addFavouriteMovieToDb(movieObj: Movies) {
        repoFavouriteMovies.addFavouriteMovieToDB(movieObj)
        fetchAllFavMovies()
    }

    fun removeFavouriteMovieFromDb(movieObj: Movies) {
        repoFavouriteMovies.removeFavouriteMovieFromDB(movieObj)
        fetchAllFavMovies()
    }

    fun getAllFavouriteMovies() : MutableLiveData<ArrayList<FavouriteMoviesEntity>> {
        if (!::favouriteMoviesLiveData.isInitialized) {
            favouriteMoviesLiveData = MutableLiveData<ArrayList<FavouriteMoviesEntity>>()
            fetchAllFavMovies()
        }
        return favouriteMoviesLiveData
    }

    private fun fetchAllFavMovies() {
        favouriteMoviesLiveData.value = repoFavouriteMovies.getAllFavouriteMovies()
    }

    fun showProgressDialog() : MutableLiveData<Boolean> {
        if (!::loader.isInitialized) {
            loader = MutableLiveData<Boolean>()
        }
        return loader
    }

    fun showError() : MutableLiveData<String> {
        if (!::displayMessage.isInitialized) {
            displayMessage = MutableLiveData<String>()
        }
        return displayMessage
    }
}