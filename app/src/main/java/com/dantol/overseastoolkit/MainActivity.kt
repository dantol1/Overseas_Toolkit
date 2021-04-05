package com.dantol.overseastoolkit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dantol.overseastoolkit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private lateinit var appBarConfiguration: AppBarConfiguration
	private lateinit var binding: ActivityMainBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

		setSupportActionBar(binding.appBar.toolbar)
		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.navController
		appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.nav_currency,
				R.id.nav_timezone,
				R.id.nav_emergency_info,
			),
			binding.drawerLayout
		)
		setupActionBarWithNavController(navController!!, appBarConfiguration)
		binding.navView.setupWithNavController(navController!!)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_about -> navController?.navigate(R.id.nav_about)
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController?.navigateUp(appBarConfiguration) == true || super.onSupportNavigateUp()
	}
}