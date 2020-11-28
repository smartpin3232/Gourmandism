package com.louis.gourmandism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.louis.gourmandism.databinding.ActivityMainBinding
import com.louis.gourmandism.home.HomeViewModel
import com.louis.gourmandism.util.CurrentFragmentType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        viewModel.currentFragmentType.value = CurrentFragmentType.HOME
        setupNavController()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                viewModel.currentFragmentType.value = CurrentFragmentType.HOME
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search->{

                viewModel.currentFragmentType.value = CurrentFragmentType.SEARCH
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalSearchFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_event->{

                viewModel.currentFragmentType.value = CurrentFragmentType.EVENT
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEventFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wishList->{

                viewModel.currentFragmentType.value = CurrentFragmentType.WISH
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalWishFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {

                viewModel.currentFragmentType.value = CurrentFragmentType.PROFILE
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            else->{

                viewModel.currentFragmentType.value = CurrentFragmentType.HOME
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                false
            }
        }

    }
    private fun setupNavController() {
    }
}