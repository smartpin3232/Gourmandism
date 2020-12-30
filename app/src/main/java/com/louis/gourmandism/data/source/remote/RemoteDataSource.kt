package com.louis.gourmandism.data.source.remote


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.DataSource
import com.louis.gourmandism.login.UserManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object RemoteDataSource : DataSource {


    override fun getLiveComments(): MutableLiveData<List<Comment>> {

        val liveData = MutableLiveData<List<Comment>>()

        FirebaseFirestore.getInstance()
            .collection("Comment")
            .addSnapshotListener { snapshot, exception ->

                val list = mutableListOf<Comment>()
                for (document in snapshot!!) {
                    Log.i("comment",document.toString())
                    val comment = document.toObject(Comment::class.java)
                    list.add(comment)
                }
                list.sortByDescending { it.createdTime }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun getComment(userId: String, mode: Int): Result<List<Comment>> =
        suspendCoroutine { continuation ->
            val db = when (mode) {

                1 -> FirebaseFirestore.getInstance()
                    .collection("Comment").whereEqualTo("host.id",userId)

                else -> FirebaseFirestore.getInstance()
                    .collection("Comment")

            }
                .get()
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

    override suspend fun newComment(comment: Comment): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Comment")
            val document = db.document()
            comment.commentId = document.id

            document
                .set(comment)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun newEvent(event: Event): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document()

            event.id = document.id
            document
                .set(event)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
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

    override suspend fun newShop(shop: Shop): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Shop")
            val document = db.document()

            shop.id = document.id
            document
                .set(shop)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun getEvent(status: Int): Result<List<Event>> =
        suspendCoroutine { continuation ->

            when (status) {
                0 -> {
                    FirebaseFirestore.getInstance()
                        .collection("Event").whereEqualTo("status", 0)
                }

                else -> {
                    FirebaseFirestore.getInstance().collection("Event")
                }
            }
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

    override fun getLiveEvents(status: Int): MutableLiveData<List<Event>> {

        val liveData = MutableLiveData<List<Event>>()

        when (status) {
            0 -> {
                FirebaseFirestore.getInstance()
                    .collection("Event").whereEqualTo("status", 0)
            }

            else -> {
                FirebaseFirestore.getInstance().collection("Event")
                    .whereArrayContains("member", UserManager.userToken.toString())
            }
        }
            .addSnapshotListener { snapshot, exception ->

                val list = mutableListOf<Event>()

                for (document in snapshot!!) {

                    val event = document.toObject(Event::class.java)
                    list.add(event)
                }
                list.sortByDescending { it.time }
                liveData.value = list
                Log.i("liveData", liveData.value.toString())
            }
        return liveData
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
                    if(!list.isNullOrEmpty()){
                        continuation.resume(Result.Success(list[0]))
                    }else{
                        continuation.resume(Result.Fail("No data"))
                    }

                } else {
                    task.exception?.let {
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                }
            }
    }

    override suspend fun getAllFriend(friendList: List<String>): Result<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("User")
            .whereIn(FieldPath.documentId(), friendList)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = task.result.toObjects(User::class.java)
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                }
            }
    }


    override suspend fun createUser(user: User): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("User")
            val document = db.document(user.id)
            document
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun getMyFavorite(userId: String): Result<MutableList<Favorite>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("Favorite")
//                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Favorite>()
                        for (document in task.result!!) {
                            val favorite = document.toObject(Favorite::class.java)
                            list.add(favorite)
                        }
                        list.sortBy { it.createdTime }
                        continuation.resume(Result.Success(list))

                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun joinGame(eventId: String, userId: String, status: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document(eventId)

            if (status == 0) {
                document.update("member", FieldValue.arrayUnion(userId))
            } else {
                document.update("member", FieldValue.arrayRemove(userId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setLike(commentId: String, userId: String, status: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Comment")
            val document = db.document(commentId)

            if (status != 0) {
                document.update("like", FieldValue.arrayUnion(userId))
            } else {
                document.update("like", FieldValue.arrayRemove(userId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setWish(folderId: String, shopId: String, status: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Favorite")
            val document = db.document(folderId)
            if (status != 0) {
                document.update("shops", FieldValue.arrayUnion(shopId))
            } else {
                document.update("shops", FieldValue.arrayRemove(shopId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun newWishList(favorite: Favorite): Result<Boolean> =
        suspendCoroutine { continuation ->

            val db = FirebaseFirestore.getInstance().collection("Favorite")
                .document()
                favorite.id = db.id

                db.set(favorite)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setBrowserHistory(userId: String, browseRecently: BrowseRecently): Result<Boolean> =
        suspendCoroutine { continuation ->

            val db = FirebaseFirestore.getInstance().collection("User")
            val document = db.document(userId)
                document.update("browseRecently", FieldValue.arrayUnion(browseRecently))

                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setSelectTag(userId: String, tag: String, status: Boolean): Result<Boolean> =
        suspendCoroutine { continuation ->

            val db = FirebaseFirestore.getInstance().collection("User")
            val document = db.document(userId)

            if(status){
                document.update("selectTags", FieldValue.arrayUnion(tag))
            } else {
                document.update("selectTags", FieldValue.arrayRemove(tag))
            }

                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun addFriend(userId: String, friendId: String, status: Boolean): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("User")
            val document = db.document(userId)

            if (status) {
                document.update("friendList", FieldValue.arrayUnion(friendId))
            } else {
                document.update("friendList", FieldValue.arrayRemove(friendId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setAttention(userId: String, favoriteId: String, status: Boolean): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Favorite")
            val document = db.document(favoriteId)

            if (status) {
                document.update("attentionList", FieldValue.arrayUnion(userId))
            } else {
                document.update("attentionList", FieldValue.arrayRemove(userId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun removeWishList(favoriteId: String): Result<Boolean> =
        suspendCoroutine { continuation ->

            val db = FirebaseFirestore.getInstance().collection("Favorite")
            val document = db.document(favoriteId)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setShareStatus(favoriteId: String, type: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Favorite")
            val document = db.document(favoriteId)
            document.update("type", type)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }


}
