package com.louis.gourmandism.data.source

import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.remote.RemoteDataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultRepository(private val remoteDataSource: DataSource,
                        private val localDataSource: DataSource):Repository{
    override suspend fun getComment(): Result<List<Comment>> {
        return remoteDataSource.getComment()
    }

    override suspend fun getShop(id: String): Result<List<Shop>> {
        return remoteDataSource.getShop(id)
    }


}
