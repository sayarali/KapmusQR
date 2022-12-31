package com.alisayar.kapmusqr.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.ActivityTeacherMainBinding

class TeacherMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_main)
        setupNavigation()
    }
    override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout)
    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, binding.drawerLayout)
        binding.navigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{ _, destination: NavDestination, _ ->
        }
    }
}