package fi.oulu.bookmarket2020

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fi.oulu.bookmarket2020.model.AppDatabase
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.content_collection.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MarketplaceActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    var appliedFilter: Int? = null
    var appliedSorting: Int? = null

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
        doAsync {
            val userId = Session(applicationContext).getLoggedInUser()!!.id!!
            val db = AppDatabase.get(applicationContext)

            val collectionBooks = when(appliedFilter) {
                R.id.filter_read -> db.collectionBookDao().getCollectionBookReadOnly(userId)
                R.id.filter_sell -> db.collectionBookDao().getCollectionBookSoldOnly(userId)
                else -> db.collectionBookDao().getCollectionBooks(userId)
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
                val adapter = MarketplaceAdapter(
                    applicationContext,
                    this@MarketplaceActivity,
                    collectionBooks
                )
                content.collection_list.adapter = adapter
            }
        }
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
