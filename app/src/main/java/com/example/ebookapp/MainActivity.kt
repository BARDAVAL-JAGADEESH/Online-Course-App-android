package com.example.ebookapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.navdrawerscratch.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.rough -> {
                    replaceFragment(RoughworkFragment())
                    true
                }
                R.id.nav_music -> {
                    replaceFragment(MusicFragment())
                    true
                }
                R.id.nav_calculator -> {
                    replaceFragment(CalculatorFragment())
                    true
                }
                R.id.nav_quize -> {
                    replaceFragment(MathQuize())
                    true
                }
                else -> false
            }
        }


        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                setToolbarTitle("Home")
            }
            R.id.nav_book -> {
                replaceFragment(BooksFragment())
                setToolbarTitle("Books")
            }
            R.id.subject_video -> {
                replaceFragment(VideoFragment())
                setToolbarTitle("Subject Video")
            }
            R.id.nav_share -> {
                replaceFragment(ShareFragment())
                setToolbarTitle("Share")
            }
            R.id.nav_aboutus -> {
                replaceFragment(AboutFragment())
                setToolbarTitle("About us")
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
