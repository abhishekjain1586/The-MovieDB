package com.eros.moviesdb.view.activities

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.eros.moviesdb.R
import com.eros.moviesdb.adapter.MoviesTabPagerAdapter
import com.eros.moviesdb.viewmodel.MainActivityViewModel
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity() {

    lateinit var viewModel : MainActivityViewModel
    lateinit var viewPager : ViewPager
    lateinit var tabs : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initViewModel()
    }

    fun initViews() {
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = MoviesTabPagerAdapter(supportFragmentManager)

        tabs = findViewById(R.id.tab_layout)
        tabs.setupWithViewPager(viewPager)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        //var searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        var searchView = (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.setSearchQuery(1, it.trim()) }
                viewPager.requestFocus()  // To remove focus from search view
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                viewModel.removeSearchResults()
                return false
            }
        })

        return true
    }
}
