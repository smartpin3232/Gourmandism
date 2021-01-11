package com.louis.gourmandism.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.louis.gourmandism.data.source.DataSource
import com.louis.gourmandism.data.source.DefaultRepository
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.data.source.local.LocalDataSource
import com.louis.gourmandism.data.source.remote.RemoteDataSource

object ServiceLocator {

    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return repository
                ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): Repository {
        return DefaultRepository(
            RemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): DataSource {
        return LocalDataSource(context)
    }
}