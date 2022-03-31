package com.abdulaziz.provalyutakurslari

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.abdulaziz.provalyutakurslari.databinding.ActivityMainBinding
import com.abdulaziz.provalyutakurslari.databinding.ItemDialogBinding
import com.abdulaziz.provalyutakurslari.fragments.CalculatorFragment
import com.abdulaziz.provalyutakurslari.fragments.CurrencyFragment
import com.abdulaziz.provalyutakurslari.fragments.HomeFragment
import com.abdulaziz.provalyutakurslari.fragments.SearchFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        setUpNavigationDrawer()
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_container,
            HomeFragment()).commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.homeFragment -> {
                    selectedFragment = HomeFragment()
                    binding.toolbar.menu.findItem(R.id.search).isVisible = false
                }
                R.id.currencyFragment -> {
                    selectedFragment = CurrencyFragment()
                    binding.toolbar.menu.findItem(R.id.search).isVisible = true

                }
                R.id.calculatorFragment -> {
                    selectedFragment = CalculatorFragment()
                    binding.toolbar.menu.findItem(R.id.search).isVisible = false

                }
            }

            showToolbar()
            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_container,
                selectedFragment!!).commit()
            true
        }

        binding.toolbar.setOnMenuItemClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.nav_host_fragment_container,
                SearchFragment()
            ).addToBackStack(null).commit()
            hideToolbar()
            true
        }


    }

    fun hideToolbar(){
        binding.toolbar.visibility = View.GONE
    }

    fun showToolbar(){
        binding.toolbar.visibility = View.VISIBLE
    }

    fun getCalculator() {
        binding.bottomNavigation.selectedItemId = R.id.calculatorFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        binding.toolbar.menu.findItem(R.id.search).isVisible = false
        return true
    }

    private fun setUpNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        toogle.isDrawerIndicatorEnabled = false
        toogle.setHomeAsUpIndicator(R.drawable.ic_drawer)
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.setToolbarNavigationClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        toogle.syncState()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (binding.bottomNavigation.selectedItemId == R.id.homeFragment ||
                binding.bottomNavigation.selectedItemId == R.id.calculatorFragment
            ) {
                finish()
            } else super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_menu -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.info_menu -> {
                val view = findViewById<View>(android.R.id.content) as ViewGroup
                val alertDialog = AlertDialog.Builder(binding.root.context, R.style.SheetDialog)
                val itemDialog = DataBindingUtil.inflate<ItemDialogBinding>(layoutInflater, R.layout.item_dialog, view, false)
                alertDialog.setView(itemDialog.root)
                alertDialog.show()
                binding.navView.isSelected = false
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}