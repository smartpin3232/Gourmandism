package com.louis.gourmandism

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.louis.gourmandism.databinding.ActivityMainBinding
import com.louis.gourmandism.databinding.NavDrawerBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.LoginActivity
import com.louis.gourmandism.login.UserManager
import com.louis.gourmandism.util.CurrentFragmentType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val loginStatus = intent.getBundleExtra("bundle")?.getBoolean("loginStatus")
        if (UserManager.userToken == null
        ) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

//        applicationContext.startService(Intent(applicationContext, GourmandismService::class.java))


        binding.textToolbarTitle.setOnClickListener {
            UserManager.clear()
        }

        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        viewModel.currentFragmentType.value = CurrentFragmentType.HOME
        setupNavController()
        setupDrawer()
    }
    private var channelId = "123"
    private fun createBuilder(pendingIntent: PendingIntent, ContentText : String): NotificationCompat.Builder? {
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.home)
            .setContentTitle("揪團通知")
            .setContentText(ContentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        return builder
    }

    private fun createNotificationChannel() {
        val name = "Event"
        val descriptionText = "Event"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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
//                    viewModel.navigate.value = 1
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
//                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_gameFragment)
                    true
                }

                R.id.nav_pick_friend -> {
//                    viewModel.navigate.value = 1
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_friendFragment)
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


}