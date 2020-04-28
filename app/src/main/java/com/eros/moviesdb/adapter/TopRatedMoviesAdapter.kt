package com.eros.moviesdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eros.moviesdb.R
import com.eros.moviesdb.model.response.Movies
import com.eros.moviesdb.utils.Constants
import com.eros.moviesdb.viewholder.TopRatedMoviesViewHolder
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TopRatedMoviesAdapter(context : Context, isTopRated : Boolean = true) : RecyclerView.Adapter<TopRatedMoviesViewHolder>() {

    private val moviesLst = ArrayList<Movies>()
    private val context = context
    private val isTopRated = isTopRated
    private var mListener : TopRatedMoviesViewHolder.OnTopRatedMoviesClickListener? = null

    fun setData(mvLst : ArrayList<Movies>) {
        this.moviesLst.clear()
        this.moviesLst.addAll(mvLst)
    }

    fun setClickListener(listener : TopRatedMoviesViewHolder.OnTopRatedMoviesClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMoviesViewHolder {
        var resource = R.layout.item_top_rated_movie
        if (!isTopRated) {
            resource = R.layout.item_searched_movie
        }
        val view : View = LayoutInflater.from(context).inflate(resource, parent, false)
        return TopRatedMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesLst.size
    }

    override fun onBindViewHolder(holder: TopRatedMoviesViewHolder, position: Int) {
        val movieData : Movies = moviesLst.get(position);

        mListener?.let { holder.bind(movieData, it) }

        holder.tvMovieTitle.text = movieData.title ?: Constants.EMPTY

        var posterWidth = Constants.POSTER_SIZE_400
        if (!isTopRated) {
            posterWidth = Constants.POSTER_SIZE_300
        }
        movieData.poster_path?.let {
            Picasso.with(context).load("https://image.tmdb.org/t/p/w"+posterWidth + it)
                .resize(posterWidth, posterWidth)
                .centerInside()
                .placeholder(context.resources.getDrawable(R.drawable.no_picture_256, null))
                .error(ContextCompat.getDrawable(context, R.drawable.no_picture_128))
                .into(holder.ivThumbnail, object : Callback {
                    override fun onSuccess() {
                        holder.ivFav.visibility = View.VISIBLE
                        if (movieData.isFavourite) {
                            holder.ivFav.setImageResource(R.drawable.favourite_enabled)
                        } else {
                            holder.ivFav.setImageResource(R.drawable.favourite_disabled_64)
                        }
                    }

                    override fun onError() {
                        holder.ivFav.visibility = View.GONE
                    }
                });
        }
    }

}