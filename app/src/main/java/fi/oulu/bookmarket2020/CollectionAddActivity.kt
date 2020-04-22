package fi.oulu.bookmarket2020

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.content.FileProvider
import com.google.api.services.books.model.Volume
import fi.oulu.bookmarket2020.bookSearch.BookSearchFragment
import fi.oulu.bookmarket2020.bookSearch.SearchListener
import fi.oulu.bookmarket2020.model.CollectionBook
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.IOException
import fi.oulu.bookmarket2020.model.AppDatabase
import org.jetbrains.anko.uiThread

class CollectionAddActivity : AppCompatActivity(), SearchListener {

    companion object {
        const val SEARCH_FRAGMENT_TAG = "isbnSearchFragment"
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    lateinit var pictureFrame: ImageView
    private var currentPicPath: String? = null
    lateinit var isbnSearchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_add)

        configureToolbar()
        initBookSearch()
        initSubmit()
        initPictureCapture()
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
        isbnSearchButton = findViewById<Button>(R.id.btn_isbn_input)

        isbnSearchButton.setOnClickListener{
            val queryText = if (isbnInput.text.toString().isEmpty())
                "9780552163361" // For testing
                else isbnInput.text.toString()

            try {
                if (searchFragment.searchByIsbn(queryText)) {
                    isbnSearchButton.isEnabled = false
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
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

                val collectionBook = CollectionBook(
                    uid = null,
                    isbn = isbn,
                    title = book.volumeInfo.title,
                    author = book.volumeInfo.authors.first(),
                    publishYear = Integer.valueOf(book.volumeInfo.publishedDate.split('-')[0]),
                    isRead = markReadCb.isChecked,
                    picturePath = currentPicPath,
                    saleBookId = null
                )

                doAsync {
                    val db = AppDatabase.get(applicationContext)

                    val uid = db.collectionBookDao().insert(collectionBook).toInt()
                    collectionBook.uid = uid

                    uiThread {
                        Toast.makeText(
                            this@CollectionAddActivity,
                            "Added book to collection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if (sellCb.isChecked) {
                        startSellBookActivity(collectionBook)
                    } else {
                        finish()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startSellBookActivity(book: CollectionBook) {

        val intent = Intent(applicationContext, SellBookActivity::class.java)
        intent.putExtra("bookId", book.uid)
        startActivity(intent)
    }

    private fun initPictureCapture() {
        pictureFrame = findViewById<ImageView>(R.id.book_picture)

        pictureFrame.setOnClickListener{
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {

                    val pictureFile: File? = try {
                        createImageFile()
                    } catch (e: IOException) {
                        null
                    }
                    pictureFile?.also {
                        val picURI: Uri = FileProvider.getUriForFile(
                            this,
                            "fi.oulu.bookmarket2020.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "bookpic_",
            ".jpg",
            storageDir
        ).apply {
            currentPicPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                val pictureBitmap = BitmapFactory.decodeFile(currentPicPath)
                pictureFrame.setImageBitmap(pictureBitmap)
            } else {
                currentPicPath = null
            }
        }
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
            findViewById<Button>(R.id.btn_submit).isEnabled = false
        }
        isbnSearchButton.isEnabled = true
    }
}