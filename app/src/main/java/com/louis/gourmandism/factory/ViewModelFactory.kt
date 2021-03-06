package com.louis.gourmandism.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.louis.gourmandism.MainViewModel
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.detail.DetailViewModel
import com.louis.gourmandism.event.item.EventItemViewModel
import com.louis.gourmandism.friend.item.FriendItemViewModel
import com.louis.gourmandism.home.HomeViewModel
import com.louis.gourmandism.login.LoginViewModel
import com.louis.gourmandism.lottery.LotteryViewModel
import com.louis.gourmandism.profile.ProfileViewModel
import com.louis.gourmandism.profile.edit.ProfileEditViewModel
import com.louis.gourmandism.search.SearchViewModel
import com.louis.gourmandism.search.create.NewTagDialogViewModel
import com.louis.gourmandism.wish.WishViewModel
import com.louis.gourmandism.wish.create.NewWishListDialogViewModel
import com.louis.gourmandism.wish.detail.WishDetailViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(repository)

                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(repository)

                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(repository)

                isAssignableFrom(WishViewModel::class.java) ->
                    WishViewModel(repository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)

                isAssignableFrom(NewWishListDialogViewModel::class.java) ->
                    NewWishListDialogViewModel(repository)

                isAssignableFrom(NewTagDialogViewModel::class.java) ->
                    NewTagDialogViewModel(repository)

                isAssignableFrom(FriendItemViewModel::class.java) ->
                    FriendItemViewModel(repository)

                isAssignableFrom(ProfileEditViewModel::class.java) ->
                    ProfileEditViewModel(repository)

                isAssignableFrom(LotteryViewModel::class.java) ->
                    LotteryViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}