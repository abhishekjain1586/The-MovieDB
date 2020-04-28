package com.eros.moviesdb.view.activities

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.eros.moviesdb.R

abstract class BaseActivity : AppCompatActivity() {

    protected var builder : AlertDialog.Builder? = null
    protected var progressDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun showDialog(title : String, msg : String) {
        if (builder == null) {
            builder = AlertDialog.Builder(this)
            builder?.setCancelable(false)
            builder?.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
        }
        builder?.setTitle(title)
        builder?.setMessage(msg)
        builder?.show()
    }

    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog?.setContentView(R.layout.progress_layout)
            progressDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            progressDialog?.setCancelable(false)
        }
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}