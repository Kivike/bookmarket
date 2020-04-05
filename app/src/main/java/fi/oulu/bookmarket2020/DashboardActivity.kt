package fi.oulu.bookmarket2020


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fi.oulu.bookmarket2020.model.CollectionBook
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_collection.*
import kotlinx.android.synthetic.main.content_collection.view.*

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

        initToolbar()
        initAddBookButton()
    }

    override fun onResume() {
        super.onResume()
        refreshCollectionList()
    }

    private fun refreshCollectionList() {
        val collection: MutableList<CollectionBook> = ArrayList()

        for (i in 1..15) {
            val book = CollectionBook(
                0,
                "abc123",
                "Test book $i",
                "Test author $i"
            )

            collection.add(book)
        }

        val adapter = CollectionAdapter(applicationContext, this, collection)
        content.collection_list.adapter = adapter
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