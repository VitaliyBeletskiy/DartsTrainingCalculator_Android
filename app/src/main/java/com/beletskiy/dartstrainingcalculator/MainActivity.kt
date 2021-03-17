package com.beletskiy.dartstrainingcalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.beletskiy.dartstrainingcalculator.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    // two variables for "clicking the back button twice to exit"
    private var backPressedTime: Long = 0
    private var backSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout

        // получаем ссылку на NavController: val navController = findNavController(R.id.fragment_container) рушится
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // подключаем Toolbar вместе с Drawer
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        // связывает меню из Drawer и навигацию (не надо писать код перехода при клике внутри Drawer-а)
        NavigationUI.setupWithNavController(binding.navView, navController)
        //binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // block Drawer if not on start destination (which is ScoreFragment)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    /**
     * if Drawer is open, the first hit on Back will close Drawer, otherwise will close the app
     * and "clicking the back button twice to exit"
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
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