package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.event.item.EventItemViewModel

@Suppress("UNCHECKED_CAST")
class EventItemViewModelFactory(
    private val repository: Repository,
    private val status: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(EventItemViewModel::class.java) ->
                    EventItemViewModel(repository, status)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}