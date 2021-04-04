package com.beletskiy.dartstrainingcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.beletskiy.dartstrainingcalculator.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // two variables for "clicking the back button twice to exit"
    private var backPressedTime: Long = 0
    private var backSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // sets the toolbar as the app bar for the activity
        setSupportActionBar(binding.toolbar)

        // setup ActionBar
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // setup Navigation Menu
        binding.navView.setupWithNavController(navController)

        // block Drawer if not on start destination (which is ScoreFragment)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    /**
     * if Drawer is open, the first hit on Back will close Drawer, otherwise will close the app
     * and "clicking the back button twice to exit"
     */
    override fun onBackPressed() {
        when {
            // if Drawer is open -> close it
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            // if current fragment is not start fragment -> default Back behavior
            navController.graph.startDestination != navController.currentDestination?.id -> {
                super.onBackPressed()
            }
            // if current fragment is start fragment -> double click Back to exit
            else -> {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backSnackbar!!.dismiss()
                    super.onBackPressed()
                    return
                } else {
                    backSnackbar = Snackbar.make(
                        binding.toolbar,
                        getString(R.string.press_back_again_to_exit),
                        Snackbar.LENGTH_SHORT
                    )
                    backSnackbar?.show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
    }
}