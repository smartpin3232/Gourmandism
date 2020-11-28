package com.louis.gourmandism.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.source.DataSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object RemoteDataSource : DataSource {

    override suspend fun getComment(): Result<List<Comment>> = suspendCoroutine{ continuation ->
        FirebaseFirestore.getInstance()
            .collection("PATH_ARTICLES")
            .orderBy("KEY_CREATED_TIME", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Comment>()
                    for (document in task.result!!) {

                        val comment = document.toObject(Comment::class.java)
                        list.add(comment)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                }
            }
    }


}
