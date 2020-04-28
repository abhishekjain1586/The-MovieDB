package com.eros.moviesdb.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eros.moviesdb.R
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity

class FavouriteMovieViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    lateinit var mListener : OnFavouriteMovieClickListener
    lateinit var mMovieItem : FavouriteMoviesEntity

    var ivFav: ImageView = itemView.findViewById(R.id.iv_fav)
    var ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    var tvMovieTitle: TextView = itemView.findViewById(R.id.tv_movie_title)

    fun bind(movieItem : FavouriteMoviesEntity, listener : OnFavouriteMovieClickListener) {
        mMovieItem = movieItem
        mListener = listener

        ivThumbnail.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.iv_thumbnail -> {
                mListener.onItemClicked(mMovieItem)
            }
        }
    }

    interface OnFavouriteMovieClickListener {
        fun onItemClicked(movieObj : FavouriteMoviesEntity)
    }
}