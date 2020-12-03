package com.louis.gourmandism.extension

import android.app.Activity
import com.louis.gourmandism.MyApplication
import com.louis.gourmandism.factory.ViewModelFactory


fun Activity.getVmFactory(): ViewModelFactory {

    val repository = (applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}
