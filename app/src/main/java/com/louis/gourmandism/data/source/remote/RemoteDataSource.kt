package com.louis.gourmandism.data.source.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.DataSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object RemoteDataSource : DataSource {


    override fun getLiveComments(): MutableLiveData<List<Comment>> {

        val liveData = MutableLiveData<List<Comment>>()

        FirebaseFirestore.getInstance()
            .collection("Comment")
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                val list = mutableListOf<Comment>()
                for (document in snapshot!!) {

                    val comment = document.toObject(Comment::class.java)
                    list.add(comment)
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun getComment(id: String, mode: Int): Result<List<Comment>> =
        suspendCoroutine { continuation ->
            val db = when (mode) {
                0 -> FirebaseFirestore.getInstance()
                    .collection("Comment")
                1 -> FirebaseFirestore.getInstance()
                    .collection("Comment")
                    .whereEqualTo("hostId", id)
                else -> FirebaseFirestore.getInstance()
                    .collection("Comment")
            }
            db.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Comment>()
                        for (document in task.result!!) {
                            val comment = document.toObject(Comment::class.java)
                            list.add(comment)
                        }
                        list.sortByDescending { it.createdTime }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }


    override suspend fun getShop(id: String, mode: Int): Result<List<Shop>> =
        suspendCoroutine { continuation ->

            val db = when (mode) {
                1 -> {
                    FirebaseFirestore.getInstance()
                        .collection("Shop")
                        .whereEqualTo("id", id)
                }

                else -> {
                    FirebaseFirestore.getInstance()
                        .collection("Shop")
                }
            }

            db.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Shop>()
                        for (document in task.result!!) {
                            val shop = document.toObject(Shop::class.java)
                            list.add(shop)
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

    override suspend fun getEvent(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("Event")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        val event = document.toObject(Event::class.java)
                        list.add(event)
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

    override suspend fun getUser(id: String): Result<User> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("User")
            .whereEqualTo("id", id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<User>()
                    for (document in task.result!!) {
                        Log.i("getUser", "${document}")
                        val user = document.toObject(User::class.java)
                        list.add(user)
                    }
                    continuation.resume(Result.Success(list.filter { it.id == id }[0]))

                } else {
                    task.exception?.let {
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                }
            }
    }


}
