package fi.oulu.bookmarket2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.google.api.services.books.model.Volume
import fi.oulu.bookmarket2020.bookSearch.BookSearchFragment
import fi.oulu.bookmarket2020.bookSearch.SearchListener
import fi.oulu.bookmarket2020.model.CollectionBook
import org.jetbrains.anko.doAsync

class CollectionAddActivity : AppCompatActivity(), SearchListener {

    companion object {
        const val SEARCH_FRAGMENT_TAG = "isbnSearchFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_add)

        configureToolbar()
        initBookSearch()
        initSubmit()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.title_collection_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initBookSearch() {
        var searchFragment = supportFragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG)
        val isbnInput = findViewById<EditText>(R.id.isbn_input)

        if (searchFragment != null) {
            isbnInput.setText((searchFragment as BookSearchFragment).getLastSearchQuery())
        } else {
            searchFragment = BookSearchFragment()
            supportFragmentManager.beginTransaction()
                .add(searchFragment, SEARCH_FRAGMENT_TAG)
                .commit()
        }
        val button = findViewById<Button>(R.id.btn_isbn_input)

        button.setOnClickListener{
            val queryText = if (isbnInput.text.toString().isEmpty())
                "9780552163361" // For testing
                else isbnInput.text.toString()

            searchFragment.searchByIsbn(queryText)
        }
    }

    private fun initSubmit()
    {
        val submitButton = findViewById<Button>(R.id.btn_submit)
        submitButton.setOnClickListener{
            val searchFragment = supportFragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG) as BookSearchFragment
            val book = searchFragment.getLastResult()

            if (book != null) {
                val markReadCb = findViewById<CheckBox>(R.id.cb_markread)
                val sellCb = findViewById<CheckBox>(R.id.cb_sell)

                val isbn = searchFragment.getLastSearchQuery()

                ///TODO picture of the book
                val collectionBook = CollectionBook(
                    uid = null,
                    isbn = isbn,
                    title = book.volumeInfo.title,
                    author = book.volumeInfo.authors.first(),
                    isRead = markReadCb.isChecked
                )

                doAsync {
                    ///TODO insert into db

                    if (sellCb.isChecked) {
                        startSellBookActivity(collectionBook)
                    }
                }
            }
        }
    }

    private fun startSellBookActivity(book: CollectionBook) {
        val intent = Intent(applicationContext, SellBookActivity::class.java)
        intent.putExtra("bookId", book.uid)
        startActivity(intent)
    }

    override fun onResult(book: Volume?) {
        val bookDetails = findViewById<Group>(R.id.book_details)

        if (book != null) {
           bookDetails.visibility = View.VISIBLE

            findViewById<TextView>(R.id.author_value).text = book.volumeInfo.authors.first()
            findViewById<TextView>(R.id.title_value).text = book.volumeInfo.title
            findViewById<TextView>(R.id.year_value).text = book.volumeInfo.publishedDate
            findViewById<Button>(R.id.btn_submit).isEnabled = true
        } else {
            bookDetails.visibility = View.GONE
        }
    }
}