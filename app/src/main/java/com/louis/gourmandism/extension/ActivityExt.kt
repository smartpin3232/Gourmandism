package com.louis.gourmandism.extension

import android.app.Activity
import android.view.View
import com.louis.gourmandism.MyApplication
import com.louis.gourmandism.factory.ViewModelFactory


fun Activity.getVmFactory(): ViewModelFactory {

    val repository = (applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

