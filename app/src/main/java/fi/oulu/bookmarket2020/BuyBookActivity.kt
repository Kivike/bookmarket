package fi.oulu.bookmarket2020

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import fi.oulu.bookmarket2020.model.AppDatabase
import fi.oulu.bookmarket2020.model.BuyBook
import fi.oulu.bookmarket2020.model.CollectionBook
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class BuyBookActivity : AppCompatActivity() {
    lateinit var buyBook: BuyBook
    lateinit var collectionBook: CollectionBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_book)
        configureToolbar()
        onBuybuttonClick()
        setBookInfo()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Buy book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setBookInfo() {
        val bookId = intent.getIntExtra("bookId", 0)
//        if (::collectionBook.isInitialized) {
            doAsync {
                val db = AppDatabase.get(applicationContext)
                collectionBook = db.collectionBookDao().getCollectionBook(bookId)

                uiThread {
    //                findViewById<TextView>(R.id.abb_edit_title).text
    //                val textView : TextView = findViewById(R.id.abb_edit_title) as TextView
    //                textView.text = testdata
                    findViewById<TextView>(R.id.abb_edit_title).text = collectionBook.title
                    findViewById<TextView>(R.id.abb_edit_author).text = collectionBook.author
                    findViewById<TextView>(R.id.abb_edit_condition).text = "test condition" // check if exists
                    findViewById<TextView>(R.id.abb_edit_price).text = "999999"
                    findViewById<TextView>(R.id.abb_edit_comment).text = "This is a test comment replace with sellbookactivity data later on"

                    findViewById<ImageView>(R.id.book_picture).setImageBitmap(
                        BookPictureLoader(applicationContext).load(collectionBook)
                    )
                }
            }

//        }
    }

    private fun onBuybuttonClick() {
        val abbBuy = findViewById<Button>(R.id.abb_buy)
        abbBuy.setOnClickListener {
            //1. goto order placed
            val intent = Intent(applicationContext, OrderSuccessActivity::class.java )
            startActivity(intent)

            //2. store in db
//            if (::buyBook.isInitialized) {
                val buyBook = BuyBook(
                    uid = null,
                    buyBookId = buyBook.uid!!,
                    title = findViewById<TextView>(R.id.abb_edit_title).toString(),
                    author = findViewById<TextView>(R.id.abb_edit_author).toString(),
                    condition = findViewById<TextView>(R.id.abb_condition).toString(),
                    price = findViewById<TextView>(R.id.abb_edit_price).toString().toFloat(),
                    comment = findViewById<TextView>(R.id.abb_edit_comment).toString()
                )

                doAsync {
                    val db = AppDatabase.get(applicationContext)
                    db.buyBookDao().insert(buyBook)
                }
//            }
        }
    }
}
