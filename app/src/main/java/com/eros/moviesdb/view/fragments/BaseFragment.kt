package com.eros.moviesdb.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    abstract fun initViews()
    lateinit var baseView : View

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
    }
}