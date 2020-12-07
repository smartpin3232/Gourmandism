package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.add2comment.Add2commentDialogViewModel
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.wish.detail.WishDetailViewModel

@Suppress("UNCHECKED_CAST")
class Add2commentViewModelFactory(
    private val repository: Repository,
    private val shop: Shop
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(Add2commentDialogViewModel::class.java) ->
                    Add2commentDialogViewModel(repository, shop)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}