package fi.oulu.bookmarket2020.bookSearch

import com.google.api.services.books.model.Volume

interface SearchListener {

    fun onResult(book: Volume?)
}