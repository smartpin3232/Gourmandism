package com.louis.gourmandism.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.DataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class LocalDataSource(val context: Context) : DataSource {
    override suspend fun getComment(userId: String,mode: Int): Result<List<Comment>> {
        TODO("Not yet implemented")
    }

    override suspend fun getShop(id: String,mode: Int): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun getLiveComments(): MutableLiveData<List<Comment>> {
        TODO("Not yet implemented")
    }


}
