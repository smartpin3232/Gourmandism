package com.louis.gourmandism.comment

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CommentViewModel(private val repository: Repository, private val comment: Comment) :ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var commentInfo = MutableLiveData<Comment>().apply {
        value = comment
    }

    private var _profile = MutableLiveData<User>()
    val profile : LiveData<User>
        get() = _profile

    init {

    }

    fun getProfile(id: String){
        coroutineScope.launch {
            val result = repository.getUser(id)
            _profile.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun setStar(list: MutableList<ImageView>, amount: Int){

        for (i in 0 until amount){
            list[i].setBackgroundResource(R.drawable.tasty_select)
        }
    }
}