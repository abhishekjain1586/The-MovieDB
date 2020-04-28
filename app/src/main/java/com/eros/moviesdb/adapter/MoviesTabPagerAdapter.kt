package com.eros.moviesdb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.eros.moviesdb.view.fragments.FavouritesFragment
import com.eros.moviesdb.view.fragments.TopRatedMoviesFragment

class MoviesTabPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                var topRatedMoviesFragment = TopRatedMoviesFragment()
                return topRatedMoviesFragment
            }

            1 -> {
                var favouritesFragment = FavouritesFragment()
                return favouritesFragment
            }
        }
        return TopRatedMoviesFragment()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "Top Rated"
            1 -> return "Favourites"
        }
        return super.getPageTitle(position)
    }

}