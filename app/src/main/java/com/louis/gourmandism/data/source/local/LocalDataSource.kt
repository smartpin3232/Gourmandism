package com.louis.gourmandism.data.source.local

import android.content.Context
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.DataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class LocalDataSource(val context: Context) : DataSource {
    override suspend fun getComment(): Result<List<Comment>> {
        TODO("Not yet implemented")
    }

    override suspend fun getShop(id: String): Result<List<Shop>> {
        TODO("Not yet implemented")
    }


}
