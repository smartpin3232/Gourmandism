package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.data.Location
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.search.create.NewShopViewModel

@Suppress("UNCHECKED_CAST")
class NewShopViewModelFactory(
    private val repository: Repository,
    private val location: Location
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NewShopViewModel::class.java) ->
                    NewShopViewModel(repository, location)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}