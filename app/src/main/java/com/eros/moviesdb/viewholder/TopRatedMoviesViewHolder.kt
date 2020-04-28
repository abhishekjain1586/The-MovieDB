package com.eros.moviesdb.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eros.moviesdb.R
import com.eros.moviesdb.model.response.Movies
import java.util.*

class TopRatedMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    lateinit var mListener : OnTopRatedMoviesClickListener
    lateinit var mMovieItem : Movies

    var ivFav: ImageView = itemView.findViewById(R.id.iv_fav)
    var ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    var tvMovieTitle: TextView = itemView.findViewById(R.id.tv_movie_title)

    fun bind(movieItem : Movies, listener : OnTopRatedMoviesClickListener) {
        mMovieItem = movieItem
        mListener = listener

        ivThumbnail.setOnClickListener(this)
        ivFav.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_thumbnail -> {
                mListener.onItemClicked(mMovieItem)
            }
            R.id.iv_fav -> {
                mListener.onFavouritesClicked(mMovieItem)
            }
        }
    }

    interface OnTopRatedMoviesClickListener {
        fun onItemClicked(movieObj : Movies)
        fun onFavouritesClicked(movieObj : Movies)
    }

}