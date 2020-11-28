package com.louis.gourmandism.data.source

import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result

interface DataSource {

    suspend fun getComment(): Result<List<Comment>>


}