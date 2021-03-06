package com.louis.gourmandism

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.louis.gourmandism.databinding.ActivityMainBinding
import com.louis.gourmandism.databinding.NavDrawerBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.LoginActivity
import com.louis.gourmandism.login.UserManager
import com.louis.gourmandism.util.CurrentFragmentType
import com.louis.gourmandism.util.DrawerToggleType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    val viewModel by viewModels<MainViewModel> { getVmFactory() }
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var PERMISSION_ID = 8888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics

        if (UserManager.userToken == null
        ) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        setupNavController()
        setupDrawer()
        getLocationPermission()
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
                    R.id.friendFragment -> CurrentFragmentType.FRIEND
                    R.id.lotteryFragment -> CurrentFragmentType.LOTTERY
                    R.id.wishDetailFragment -> CurrentFragmentType.WISH_DETAIL
                    R.id.commentFragment -> CurrentFragmentType.COMMENT
                    else -> viewModel.currentFragmentType.value
                }
            }
    }

    private fun setupDrawer() {
        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_pick_restaurant -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_lotteryFragment)
                    true
                }

                R.id.nav_friend -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_friendFragment)
                    true
                }

                R.id.nav_sign_out -> {

                    UserManager.clear()
                    Toast.makeText(this, "已登出", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false
        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
//            toolbar.setNavigationIcon(R.drawable.ic_menu)
        }
        val bindingNavHeader = NavDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false)
        bindingNavHeader.lifecycleOwner = this
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)
        bindingNavHeader.viewModel = viewModel

        viewModel.currentDrawerToggleType.observe(this, Observer { type ->
            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
            binding.toolbar.setNavigationIcon(
                when (type) {
                    DrawerToggleType.BACK -> R.drawable.back
                    else -> R.drawable.menu
                }
            )
            actionBarDrawerToggle?.setToolbarNavigationClickListener {
                when (type) {
                    DrawerToggleType.BACK -> findNavController(R.id.myNavHostFragment).navigateUp()
                    else -> {}
                }
            }
        })
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

                    UserManager.userToken?.let {
                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment(it))
                    }
                    return@OnNavigationItemSelectedListener true
                }
                else -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                    false
                }
            }

        }

    private var firstPressedTime: Long = 0
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if(viewModel.currentFragmentType.value == CurrentFragmentType.HOME){
                if (System.currentTimeMillis() - firstPressedTime < 2000) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(this@MainActivity, "再按一次退出", Toast.LENGTH_SHORT).show()
                    firstPressedTime = System.currentTimeMillis()
                }
            }else{
                super.onBackPressed()
            }
        }
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_ID)
        }
    }
}