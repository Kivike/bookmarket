package fi.oulu.bookmarket2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
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

    var appliedFilter: Int? = null
    var appliedSorting: Int? = null

    lateinit var filterPopupMenu: PopupMenu
    lateinit var sortingPopupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.nav_view)

        initToolbar()
        initAddBookButton()
        initFilters()
        initSorting()
    }

    override fun onResume() {
        super.onResume()
        refreshCollection()
    }

    private fun refreshCollection() {
        doAsync {
            val db = AppDatabase.get(applicationContext)

            val collectionBooks = when(appliedFilter) {
                R.id.filter_read -> db.collectionBookDao().getCollectionBookReadOnly()
                R.id.filter_sell -> db.collectionBookDao().getCollectionBookSoldOnly()
                else -> db.collectionBookDao().getCollectionBooks()
            }.toMutableList()

            collectionBooks.sortBy{ it.publishYear }

            when(appliedSorting) {
                R.id.sorting_author_asc -> collectionBooks.sortBy { it.author }
                R.id.sorting_author_desc -> collectionBooks.sortByDescending { it.author }
                R.id.sorting_title_asc -> collectionBooks.sortBy { it.title }
                R.id.sorting_title_desc -> collectionBooks.sortByDescending { it.title }
                R.id.sorting_published_asc -> collectionBooks.sortBy { it.publishYear }
                R.id.sorting_published_desc -> collectionBooks.sortByDescending { it.publishYear }
            }

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

    /**
     * Initialize collection filter selection
     */
    private fun initFilters() {
        filterPopupMenu = PopupMenu(this, collection_filters)
        filterPopupMenu.menuInflater.inflate(R.menu.filter_menu, filterPopupMenu.menu)

        for (menuItem in filterPopupMenu.menu.children) {
            if (menuItem.isChecked) {
                appliedFilter = menuItem.itemId
                break
            }
        }
        filterPopupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            setCollectionFilter(item!!)
            true
        }
        collection_filters.setOnClickListener {
            filterPopupMenu.show()
        }
    }

    /**
     * Initialize collection sorting selection
     */
    private fun initSorting() {
        sortingPopupMenu = PopupMenu(this, collection_filters)
        sortingPopupMenu.menuInflater.inflate(R.menu.sorting_menu, sortingPopupMenu.menu)

        for (menuItem in sortingPopupMenu.menu.children) {
            if (menuItem.isChecked) {
                appliedSorting = menuItem.itemId
                break
            }
        }
        sortingPopupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            setCollectionSorting(item!!)
            true
        }
        collection_sorting.setOnClickListener{
            sortingPopupMenu.show()
        }
    }

    /**
     * Set filter option to be used
     */
    private fun setCollectionFilter(item: MenuItem) {
        item.isChecked = true
        appliedFilter = item.itemId
        refreshCollection()
    }

    /**
     * Set sorting option
     */
    private fun setCollectionSorting(item: MenuItem) {
        item.isChecked = true
        appliedSorting = item.itemId
        refreshCollection()
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