package com.socket9.thetsl.activities

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_app_bar.*
import org.jetbrains.anko.AnkoLogger

/**
 * Created by Euro (ripzery@gmail.com) on 5/12/16 AD.
 */

open class ToolbarActivity : AppCompatActivity(), AnkoLogger {
    fun setupToolbar(title: String) {
        setSupportActionBar(toolbar)
        toolbarTitle.text = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}