package com.louis.gourmandism

import android.app.Application
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.util.ServiceLocator
import kotlin.properties.Delegates

class MyApplication : Application() {

    val repository: Repository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: MyApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}