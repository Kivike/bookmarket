package fi.oulu.bookmarket2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.content_collection.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MarketplaceActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        initToolbar()
    }

    override fun onResume() {
        super.onResume()
        refreshCollectionList()
    }

    private fun refreshCollectionList() {
        val reminders: MutableList<String> = ArrayList()

        for (i in 1..15) {
            reminders.add("Book name${i}")
        }

        val adapter = MarketplaceAdapter(applicationContext, reminders)
        content.collection_list.adapter = adapter
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navFragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .add(navFragment, CollectionAddActivity.SEARCH_FRAGMENT_TAG)
            .commit()

        navView.setNavigationItemSelectedListener(navFragment)

        doAsync {
            val sessionUserName = Session(applicationContext).getLoggedInUser()!!.name

            uiThread {
                val navView = findViewById<NavigationView>(R.id.nav_view)
                val header = navView.getHeaderView(0)
                header.findViewById<TextView>(R.id.greeting_text).text = getString(
                    R.string.text_hello,
                    sessionUserName
                )
            }
        }
    }
}
