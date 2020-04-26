package fi.oulu.bookmarket2020

import android.content.Intent
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
import fi.oulu.bookmarket2020.model.CollectionBook
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.content_collection.*
import kotlinx.android.synthetic.main.content_collection.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CollectionActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    private var appliedFilter: Int? = null
    private var appliedSorting: Int? = null

    private lateinit var filterPopupMenu: PopupMenu
    private lateinit var sortingPopupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

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
            val userId = Session(applicationContext).getLoggedInUser()!!.id!!

            val db = AppDatabase.get(applicationContext)

            val collectionBooks = when (appliedFilter) {
                R.id.filter_read -> db.collectionBookDao().getCollectionBookReadOnly(userId)
                R.id.filter_sell -> db.collectionBookDao().getCollectionBookSoldOnly(userId)
                else -> db.collectionBookDao().getCollectionBooks(userId)
            }.toMutableList()

            when (appliedSorting) {
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
                    this@CollectionActivity,
                    collectionBooks
                )
                content.collection_list.adapter = adapter
                collection_label.text = resources.getQuantityString(
                    R.plurals.number_of_books,
                    collectionBooks.size,
                    collectionBooks.size
                )
            }
            addSampleData()
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
        sortingPopupMenu = PopupMenu(this, collection_sorting)
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
        collection_sorting.setOnClickListener {
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

    /**
     * Init toolbar and navigation drawer
     */
    private fun initToolbar() {
        toolbar.setTitle(R.string.collection)

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

    /**
     * Initialize FAB that starts activity to add a book to collection
     */
    private fun initAddBookButton() {
        fab_add_book.setOnClickListener {
            val intent = Intent(applicationContext, CollectionAddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addSampleData() {
        val isbn = "1880418622"
        val publisherDate = 2004

        doAsync {
            val session = Session(applicationContext)
            val userId = session.getLoggedInUser()!!.id!!

            val bookCollections = AppDatabase.get(applicationContext).collectionBookDao()

            // check if the book has been already added or not
            val getCollectionBooks = bookCollections.getCollectionBooks(userId).size


            if (getCollectionBooks < 1) {
                val collectionBook = CollectionBook(
                    isbn = isbn,
                    title = "The Dark Tower VII",
                    author = "Stephen King",
                    publishYear = publisherDate,
                    isRead = true,
                    picturePath = "drawable://" + R.drawable.darktower7,
                    saleBookId = null,
                    ownerId = userId
                )

                val createEntry = bookCollections.insert(collectionBook)
                uiThread {
                    @Suppress("UNUSED_EXPRESSION")
                    createEntry
                }
            }
        }
    }
}