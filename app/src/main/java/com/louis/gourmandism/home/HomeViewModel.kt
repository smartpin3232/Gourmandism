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

    private var _commentList =MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private var _navigateDetailInfo =MutableLiveData<String>()
    val navigateDetailInfo: LiveData<String>
        get() = _navigateDetailInfo

    private var _navigateCommentInfo =MutableLiveData<Comment>()
    val navigateCommentInfo: LiveData<Comment>
        get() = _navigateCommentInfo

    private var _navigateProfileInfo =MutableLiveData<String>()
    val navigateProfileInfo: LiveData<String>
        get() = _navigateProfileInfo

    private var _likeStatus =MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getLiveCommentResult()
    }

    fun setLike(commentId: String, status: Int){
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setLike(commentId, it, status)
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

    private fun getLiveCommentResult() {
        _commentList = repository.getLiveComments()
    }

    fun navigationToDetail(item:String){
        _navigateDetailInfo.value = item
    }

    fun navigateToComment(comment: Comment) {
        _navigateCommentInfo.value = comment
    }

    fun navigateToProfile(userId: String) {
        _navigateProfileInfo.value = userId
    }

    fun onNavigationDone() {
        _navigateCommentInfo.value = null
        _navigateProfileInfo.value = null
        _navigateDetailInfo.value = null
    }

}