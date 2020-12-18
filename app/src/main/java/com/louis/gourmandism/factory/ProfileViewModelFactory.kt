package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.add2comment.Add2commentDialogViewModel
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.profile.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val repository: Repository,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository, userId)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}