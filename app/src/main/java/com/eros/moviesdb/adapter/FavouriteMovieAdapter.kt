package com.eros.moviesdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eros.moviesdb.R
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity
import com.eros.moviesdb.utils.Constants
import com.eros.moviesdb.viewholder.FavouriteMovieViewHolder
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FavouriteMovieAdapter (context : Context) : RecyclerView.Adapter<FavouriteMovieViewHolder>() {

    private val moviesLst = ArrayList<FavouriteMoviesEntity>()
    private val context = context
    private var mListener : FavouriteMovieViewHolder.OnFavouriteMovieClickListener? = null

    fun setData(mvLst : ArrayList<FavouriteMoviesEntity>) {
        this.moviesLst.clear()
        this.moviesLst.addAll(mvLst)
    }

    fun setClickListener(listener : FavouriteMovieViewHolder.OnFavouriteMovieClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMovieViewHolder {
        var resource = R.layout.item_favourite_movies
        val view : View = LayoutInflater.from(context).inflate(resource, parent, false)
        return FavouriteMovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesLst.size
    }

    override fun onBindViewHolder(holder: FavouriteMovieViewHolder, position: Int) {
        val movieData : FavouriteMoviesEntity = moviesLst.get(position);

        mListener?.let { holder.bind(movieData, it) }

        holder.ivFav.visibility = View.GONE
        holder.tvMovieTitle.text = movieData.movieTitle ?: Constants.EMPTY

        var posterWidth = Constants.POSTER_SIZE_400
        movieData.posterPath?.let {
            Picasso.with(context).load("https://image.tmdb.org/t/p/w"+posterWidth + it)
                .resize(posterWidth, posterWidth)
                .placeholder(context.resources.getDrawable(R.drawable.no_picture_256, null))
                .centerInside()
                .error(ContextCompat.getDrawable(context, R.drawable.no_picture_128))
                .into(holder.ivThumbnail);

        }
    }
}