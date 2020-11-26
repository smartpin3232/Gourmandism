package com.louis.gourmandism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.louis.gourmandism.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this

        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search->{

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalSearchFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_event->{

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEventFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wishList->{

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalWishFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
//            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
//                R.id.homeFragment -> CurrentFragmentType.HOME
//                R.id.catalogFragment -> CurrentFragmentType.CATALOG
//                R.id.cartFragment -> CurrentFragmentType.CART
//                R.id.profileFragment -> CurrentFragmentType.PROFILE
//                R.id.detailFragment -> CurrentFragmentType.DETAIL
//                R.id.paymentFragment -> CurrentFragmentType.PAYMENT
//                R.id.checkoutSuccessFragment -> CurrentFragmentType.CHECKOUT_SUCCESS
//                else -> viewModel.currentFragmentType.value
//            }
        }
    }
}