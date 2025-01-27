package com.example.buynow.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.example.buynow.R
import com.example.buynow.presentation.fragment.*
import com.example.buynow.utils.FirebaseUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize the BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Load the default fragment
        supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, HomeFragment()).commit()

        // Initialize the logout button
        val logoutBtn: Button = findViewById(R.id.btnLogout)
        logoutBtn.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Sign out from Firebase
        FirebaseUtils.firebaseAuth.signOut()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Finish the current activity
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.homeMenu -> HomeFragment()
            R.id.shopMenu -> ShopFragment()
            R.id.bagMenu -> BagFragment()
            R.id.favMenu -> FavFragment()
            R.id.profileMenu -> ProfileFragment()
            else -> null
        }
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_fragment, it, it.javaClass.simpleName)
                .commit()
            return true
        }
        return false
    }
}
