package com.eros.moviesdb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private lateinit var favouriteMoviesLiveData : MutableLiveData<ArrayList<FavouriteMoviesEntity>>
    private lateinit var loader : MutableLiveData<Boolean>
    private lateinit var displayMessage : MutableLiveData<String>

    private var searchedQuery = Constants.EMPTY
    private var oldSearchedQuery = Constants.EMPTY

    private var pageNoTopRatedMovies = 1
    private var pageNoSearchedMovies = 1


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


    /* For Top rated movies*/
    fun getTopRatedMovies() : MutableLiveData<TopRatedMoviesRes> {
        if (!::topRatedMoviesLiveData.isInitialized) {
            topRatedMoviesLiveData = MutableLiveData<TopRatedMoviesRes>()
            repoTopRatedMovies.setListener(this)
            getTopRatedMovFromRepo(1)
        }
        return topRatedMoviesLiveData
    }

    fun toLoadMoreTopRatedMovies() : Boolean {
        val res = topRatedMoviesLiveData.value

        return res?.let {
                it/*res? - naming as it*/ -> it.page?.let {
                it1/*it.page? - naming as it1*/ -> it.total_pages?.let {
                it2/*it.total_pages? - naming as it2*/ -> it1 < it2
        }
        }
        } ?: false
    }

    fun loadMoreTopRatedMovies() {
        val res = topRatedMoviesLiveData.value

        res?.let {
            it/*res? - naming as it*/ -> it.page?.let {
                it1/*it.page? - naming as it1*/ -> it.total_pages?.let {
                    it2/*it.total_pages? - naming as it2*/ -> if (it1 < it2) {
                        getTopRatedMovFromRepo(++pageNoTopRatedMovies)
                    }
                }
            }
        }
    }

    private fun getTopRatedMovFromRepo(page : Int) {
        loader.value = true
        repoTopRatedMovies.getTopRatedMovies(page)
    }

    override fun onTopRatedMoviesSuccess(response: TopRatedMoviesRes) {
        loader.value = false

        val tempMovieLst = arrayListOf<Movies>()

        topRatedMoviesLiveData.value?.moviesLst?.let {
            tempMovieLst.addAll(it)
        }
        response.moviesLst?.let {
            tempMovieLst.addAll(it)
            it.clear()
            it.addAll(tempMovieLst)
        }

        // updating favourites in result
        val favMovies = repoFavouriteMovies.getAllFavouriteMovies()
        favMovies?.let {
            for (favObj in favMovies) {
                for (movieObj in tempMovieLst) {
                    if (favObj.movieId == movieObj.id) {
                        movieObj.isFavourite = true
                        break
                    }
                }
            }
        }
        /* End */

        topRatedMoviesLiveData.value = response
    }

    override fun onTopRatedMoviesFailure(errorMsg: String) {
        loader.value = false
        displayMessage.value = errorMsg
    }
    /* End */


    /* For Searched Movies*/
    fun getSearchedMovies() : MutableLiveData<TopRatedMoviesRes> {
        if (!::searchedMoviesLiveData.isInitialized) {
            searchedMoviesLiveData = MutableLiveData<TopRatedMoviesRes>()
            repoSearchedMovies.setListener(this)
        }
        return searchedMoviesLiveData
    }

    fun toLoadMoreSearchedMovies() : Boolean {
        val res = searchedMoviesLiveData.value

        return res?.let {
                it/*res? - naming as it*/ -> it.page?.let {
                it1/*it.page? - naming as it1*/ -> it.total_pages?.let {
                it2/*it.total_pages? - naming as it2*/ -> it1 < it2
        }
        }
        } ?: false
    }

    fun loadMoreSearchedMovies() {
        val res = searchedMoviesLiveData.value

        res?.let {
            it/*res? - naming as it*/ -> it.page?.let {
                it1/*it.page? - naming as it1*/ -> it.total_pages?.let {
                    it2/*it.total_pages? - naming as it2*/ -> if (it1 < it2) {
                        setSearchQuery(++pageNoSearchedMovies, searchedQuery)
                    }
                }
            }
        }
    }

    fun setSearchQuery(page : Int, query : String) {
        loader.value = true
        searchedQuery = query
        repoSearchedMovies.getSearchedMovies(page, query)
    }

    override fun onSearchedMoviesSuccess(response: TopRatedMoviesRes) {
        loader.value = false
        val tempMovieLst = arrayListOf<Movies>()

        if (oldSearchedQuery.equals(searchedQuery, true)) {
            searchedMoviesLiveData.value?.moviesLst?.let {
                tempMovieLst.addAll(it)
            }
            response.moviesLst?.let {
                tempMovieLst.addAll(it)
                it.clear()
                it.addAll(tempMovieLst)
            }
        }

        // updating favourites in result
        val favMovies = repoFavouriteMovies.getAllFavouriteMovies()
        favMovies?.let {
            it -> response.moviesLst?.let {
                it1 ->
                    for (favObj in it) {
                        for (movieObj in it1) {
                            if (favObj.movieId == movieObj.id) {
                                movieObj.isFavourite = true
                                break
                            }
                        }
                    }
            }
        }
        // End

        searchedMoviesLiveData.value = response
        oldSearchedQuery = searchedQuery
    }

    override fun onSearchedMoviesFailure(errorMsg: String) {
        loader.value = false
        displayMessage.value = errorMsg
    }

    fun removeSearchResults() {
        searchedMoviesLiveData.value = searchedMoviesLiveData.value?.moviesLst?.let {
            it.clear()
            pageNoSearchedMovies = 1
            searchedMoviesLiveData.value
        }
    }
    /* End */


    /* For Favourites */
    fun addFavouriteMovieToDb(movieObj: Movies) {
        loader.value = true
        Thread(object : Runnable {
            override fun run() {
                repoFavouriteMovies.addFavouriteMovieToDB(movieObj)
                topRatedMoviesLiveData.value?.moviesLst?.let {
                    for (item in it) {
                        if (item.id == movieObj.id) {
                            item.isFavourite = true
                            topRatedMoviesLiveData.postValue(topRatedMoviesLiveData.value)
                            break
                        }
                    }
                }.also {
                    searchedMoviesLiveData.value?.moviesLst?.let {
                        if (it.isNotEmpty()) {
                            for (item in it) {
                                if (item.id == movieObj.id) {
                                    item.isFavourite = true
                                    searchedMoviesLiveData.postValue(searchedMoviesLiveData.value)
                                    break
                                }
                            }
                        }
                    }
                }
                fetchAllFavMovies()
                loader.postValue(false)
            }
        }).start()
    }

    fun removeFavouriteMovieFromDb(movieObj: Movies) {
        loader.value = true
        Thread(object : Runnable {
            override fun run() {
                repoFavouriteMovies.removeFavouriteMovieFromDB(movieObj)
                topRatedMoviesLiveData.value?.moviesLst?.let {
                    for (item in it) {
                        if (item.id == movieObj.id) {
                            item.isFavourite = false
                            topRatedMoviesLiveData.postValue(topRatedMoviesLiveData.value)
                            break
                        }
                    }
                }.also {
                    searchedMoviesLiveData.value?.moviesLst?.let {
                        if (it.isNotEmpty()) {
                            for (item in it) {
                                if (item.id == movieObj.id) {
                                    item.isFavourite = false
                                    searchedMoviesLiveData.postValue(searchedMoviesLiveData.value)
                                    break
                                }
                            }
                        }
                    }
                }
                fetchAllFavMovies()
                loader.postValue(false)
            }
        }).start()
    }

    fun getAllFavouriteMovies() : MutableLiveData<ArrayList<FavouriteMoviesEntity>> {
        if (!::favouriteMoviesLiveData.isInitialized) {
            favouriteMoviesLiveData = MutableLiveData<ArrayList<FavouriteMoviesEntity>>()
            fetchAllFavMovies()
        }
        return favouriteMoviesLiveData
    }

    private fun fetchAllFavMovies() {
        favouriteMoviesLiveData.postValue(repoFavouriteMovies.getAllFavouriteMovies())
    }
    /* End */


}