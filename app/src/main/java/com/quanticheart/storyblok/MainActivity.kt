package com.quanticheart.storyblok

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quanticheart.storyblok.conn.model.BottomMenu
import com.quanticheart.storyblok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.list.observe(this) {
            createMenu(it)
            binding.flipper.displayedChild = 1
        }
        viewModel.loadBottomMenu()
    }

    inner class HomeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "REFRESH_HOME" -> viewModel.loadBottomMenu()
            }
        }
    }

    private var rec: HomeReceiver? = null
    override fun onResume() {
        super.onResume()
        val f = IntentFilter("REFRESH_HOME")
        if (rec == null) {
            rec = HomeReceiver()
        }
        registerReceiver(rec, f)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(rec)
        rec = null
    }

    private fun createMenu(bottomMenus: List<BottomMenu>) {
        if (bottomMenus.isNotEmpty()) {
            val menu: Menu = binding.navView.menu
            menu.clear()

            bottomMenus.forEachIndexed { index, item ->
                when (item.id) {
                    "home" -> createMenuItem(
                        menu,
                        index,
                        item.title,
                        R.drawable.ic_home_black_24dp,
                        R.id.navigation_home
                    )
                    "images" -> createMenuItem(
                        menu,
                        index,
                        item.title,
                        R.drawable.ic_notifications_black_24dp,
                        R.id.navigation_notifications
                    )
                    "dashboard" -> createMenuItem(
                        menu,
                        index,
                        item.title,
                        R.drawable.ic_dashboard_black_24dp,
                        R.id.navigation_dashboard
                    )
                    "email" -> createMenuItem(
                        menu,
                        index,
                        item.title,
                        R.drawable.ic_baseline_mail_24,
                        R.id.emailFragment
                    )
                    "profile" -> createMenuItem(
                        menu,
                        index,
                        item.title,
                        R.drawable.ic_baseline_miscellaneous_services_24,
                        R.id.profileFragment
                    )
                }
            }
        }

        binding.navView.visibility = View.VISIBLE
    }

    private fun createMenuItem(
        menu: Menu,
        index: Int,
        title: String,
        @DrawableRes ic: Int,
        navigation: Int
    ) {
        menu.add(Menu.NONE, index, Menu.NONE, title)
            .setIcon(ic)
            .setOnMenuItemClickListener {
                menu.getItem(index).isChecked = true
                navController.navigate(navigation)
                true
            }

        if (index == 0) {
            menu.getItem(0).isChecked = true
            navController.navigate(navigation)
        }
    }
}
