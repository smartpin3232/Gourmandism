package com.louis.gourmandism.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.louis.gourmandism.data.source.DataSource
import com.louis.gourmandism.data.source.DefaultRepository
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.data.source.local.LocalDataSource
import com.louis.gourmandism.data.source.remote.RemoteDataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * A Service Locator for the [PublisherRepository].
 */
object ServiceLocator {

    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return repository
                ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): Repository {
        return DefaultRepository(
            RemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): DataSource {
        return LocalDataSource(context)
    }
}