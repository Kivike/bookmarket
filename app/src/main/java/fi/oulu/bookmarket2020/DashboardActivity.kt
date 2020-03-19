package fi.oulu.bookmarket2020


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.nav_view)


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        configureMarketplaceSearch()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "Clickity click", Toast.LENGTH_SHORT).show()

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun configureMarketplaceSearch() {
        searchView = findViewById(R.id.search_marketplace)
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        searchView.queryHint = "Search marketplace"
    }
}