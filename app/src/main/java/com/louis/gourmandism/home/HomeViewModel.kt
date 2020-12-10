package com.louis.gourmandism.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigationStatus =MutableLiveData<String>()
    val navigationStatus: LiveData<String>
        get() = _navigationStatus

    var commentList = MutableLiveData<List<Comment>>()

    var toCommentStatus = MutableLiveData<Comment>()

    private var _likeStatus =MutableLiveData<Boolean>()
    val likeStatus: LiveData<Boolean>
        get() = _likeStatus

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getLiveArticlesResult()
    }

    private fun getLiveArticlesResult() {
        commentList = repository.getLiveComments()
    }

    fun navigationToDetail(item:String){
        _navigationStatus.value = item
    }

    fun onNavigated() {
        _navigationStatus.value = null
    }

    fun navigateToComment(comment: Comment) {
        toCommentStatus.value = comment
    }

    fun setLike(commentId: String, status: Int){
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setlike(commentId, it, status)
                _likeStatus.value = when(result){
                    is Result.Success -> {
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            }
        }
    }



}