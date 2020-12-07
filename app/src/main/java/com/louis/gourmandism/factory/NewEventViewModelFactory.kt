package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.add2comment.Add2commentDialogViewModel
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.event.create.NewEventViewModel

@Suppress("UNCHECKED_CAST")
class NewEventViewModelFactory(
    private val repository: Repository,
    private val shop: Shop
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NewEventViewModel::class.java) ->
                    NewEventViewModel(repository, shop)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}