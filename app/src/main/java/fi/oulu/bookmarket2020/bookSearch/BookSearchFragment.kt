package fi.oulu.bookmarket2020.bookSearch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.api.services.books.model.Volume
import kotlin.Exception

class BookSearchFragment : Fragment(), SearchListener {

    private var lastIsbn: String = ""
    private var searchTask: SearchTask? = null
    private var searchListener: SearchListener? = null

    private var isSearching: Boolean = false

    private var lastResult: Volume? = null

    /**
     * @return Boolean Returns true if search was started
     * @throws Exception
     */
    fun searchByIsbn(isbn: String): Boolean {
        val validIsbn = formatIsbn(isbn)

        if (validIsbn == lastIsbn) {
            return isSearching
        }

        if (searchTask != null) {
            searchTask!!.cancel(true)
        }
        lastIsbn = validIsbn
        searchTask = SearchTask()
        searchTask!!.listener = this
        searchTask!!.execute(validIsbn)
        isSearching = true
        return isSearching
    }

    private fun formatIsbn(isbn: String): String {
        isbn.replace("-", "")

        if (isbn.length != 10 && isbn.length != 13) {
            throw Exception("Not a valid ISBN number")
        }
        return isbn
    }

    fun getLastSearchQuery(): String {
        return lastIsbn
    }

    fun getLastResult(): Volume? {
        return lastResult
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        searchListener = context as SearchListener
    }

    override fun onResult(book: Volume?) {
        isSearching = false
        lastResult = book
        searchListener?.onResult(book)
    }
}