package fi.oulu.bookmarket2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import fi.oulu.bookmarket2020.model.AppDatabase
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_collection.*
import kotlinx.android.synthetic.main.content_collection.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.nav_view)

        initToolbar()
        initAddBookButton()
    }

    override fun onResume() {
        super.onResume()
        refreshCollection()
    }

    private fun refreshCollection() {
        doAsync {
            val db = AppDatabase.get(applicationContext)
            val collectionBooks = db.collectionBookDao().getCollectionBooks().toMutableList()

            uiThread {
                val adapter = CollectionAdapter(
                    applicationContext,
                    this@DashboardActivity,
                    collectionBooks
                )
                content.collection_list.adapter = adapter
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "Clickity click", Toast.LENGTH_SHORT).show()

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun initAddBookButton() {
        fab_add_book.setOnClickListener{
            val intent = Intent(applicationContext, CollectionAddActivity::class.java)
            startActivity(intent)
        }
    }
}