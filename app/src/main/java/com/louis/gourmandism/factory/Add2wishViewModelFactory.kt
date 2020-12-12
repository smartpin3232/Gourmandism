package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.add2comment.Add2commentDialogViewModel
import com.louis.gourmandism.add2wish.Add2wishViewModel
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository

@Suppress("UNCHECKED_CAST")
class Add2wishViewModelFactory(
    private val repository: Repository,
    private val shopId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(Add2wishViewModel::class.java) ->
                    Add2wishViewModel(repository, shopId)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}