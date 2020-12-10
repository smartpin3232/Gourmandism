package com.louis.gourmandism

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.louis.gourmandism.databinding.ActivityMainBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.LoginActivity
import com.louis.gourmandism.login.UserManager
import com.louis.gourmandism.util.CurrentFragmentType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginStatus = intent.getBundleExtra("bundle")?.getBoolean("loginStatus")
        if (UserManager.userToken==null && loginStatus==null
        ) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.textToolbarTitle.setOnClickListener {
            UserManager.clear()
        }

        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        viewModel.currentFragmentType.value = CurrentFragmentType.HOME
        setupNavController()
    }


    private fun setupNavController() {
        this.findNavController(R.id.myNavHostFragment)
            .addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
                viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                    R.id.homeFragment -> CurrentFragmentType.HOME
                    R.id.searchFragment -> CurrentFragmentType.SEARCH
                    R.id.eventFragment -> CurrentFragmentType.EVENT
                    R.id.wishFragment -> CurrentFragmentType.WISH
                    R.id.profileFragment -> CurrentFragmentType.PROFILE
                    R.id.detailFragment -> CurrentFragmentType.DETAIL
                    else -> viewModel.currentFragmentType.value
                }
            }
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalSearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_event -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEventFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_wishList -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalWishFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
                else -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                    false
                }
            }

        }


}