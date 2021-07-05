package fi.oulu.bookmarket2020

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fi.oulu.bookmarket2020.model.AppDatabase
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.content_collection.view.*
import kotlinx.android.synthetic.main.content_marketplace.*
import kotlinx.android.synthetic.main.content_marketplace.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MarketplaceActivity : AppCompatActivity() {

    companion object {
        const val NAV_FRAGMENT_TAG = "navFragment"
    }

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    private var appliedSorting: Int? = null

    private lateinit var sortingPopupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        initToolbar()
        initSorting()
    }

    override fun onResume() {
        super.onResume()
        refreshMarketplaceList()
    }

    /**
     * Refresh books in marketplace listing
     */
    private fun refreshMarketplaceList() {
        doAsync {
            val userId = Session(applicationContext).getLoggedInUser()!!.id!!
            val db = AppDatabase.get(applicationContext)

            val marketplaceBooks = db.marketplaceBookDao().getMarketplaceBooks(userId).toMutableList()

            when(appliedSorting) {
                R.id.sorting_author_asc -> marketplaceBooks.sortBy { it.collectionBook.author }
                R.id.sorting_author_desc -> marketplaceBooks.sortByDescending { it.collectionBook.author }
                R.id.sorting_title_asc -> marketplaceBooks.sortBy { it.collectionBook.title }
                R.id.sorting_title_desc -> marketplaceBooks.sortByDescending { it.collectionBook.title }
                R.id.sorting_published_asc -> marketplaceBooks.sortBy { it.collectionBook.publishYear }
                R.id.sorting_published_desc -> marketplaceBooks.sortByDescending { it.collectionBook.publishYear }
            }

            uiThread {
                val adapter = MarketplaceAdapter(
                    applicationContext,
                    this@MarketplaceActivity,
                    marketplaceBooks
                )
                content.marketplace_list.adapter = adapter
            }
        }
    }

    /**
     * Initialize collection sorting selection
     */
    private fun initSorting() {
        sortingPopupMenu = PopupMenu(this, marketplace_sorting)
        sortingPopupMenu.menuInflater.inflate(R.menu.sorting_menu, sortingPopupMenu.menu)

        for (menuItem in sortingPopupMenu.menu.children) {
            if (menuItem.isChecked) {
                appliedSorting = menuItem.itemId
                break
            }
        }
        sortingPopupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            setMarketplaceSorting(item!!)
            true
        }
        marketplace_sorting.setOnClickListener {
            sortingPopupMenu.show()
        }
    }

    /**
     * Set sorting option
     */
    private fun setMarketplaceSorting(item: MenuItem) {
        item.isChecked = true
        appliedSorting = item.itemId
        refreshMarketplaceList()
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.marketplace)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navFragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .add(navFragment, NAV_FRAGMENT_TAG)
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
