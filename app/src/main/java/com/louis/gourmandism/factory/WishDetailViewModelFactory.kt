package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.wish.detail.WishDetailViewModel

@Suppress("UNCHECKED_CAST")
class WishDetailViewModelFactory(
    private val repository: Repository,
    private val favorite: Favorite
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(WishDetailViewModel::class.java) ->
                    WishDetailViewModel(repository, favorite)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}