package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.comment.CommentViewModel
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.event.create.NewEventViewModel

@Suppress("UNCHECKED_CAST")
class CommentViewModelFactory(
    private val repository: Repository,
    private val comment: Comment
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CommentViewModel::class.java) ->
                    CommentViewModel(repository, comment)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}