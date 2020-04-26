package fi.oulu.bookmarket2020

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import fi.oulu.bookmarket2020.model.AppDatabase
import fi.oulu.bookmarket2020.model.MarketplaceBook
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class BuyBookActivity : AppCompatActivity() {
    lateinit var marketplaceBook: MarketplaceBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_book)
        configureToolbar()
        onBuyButtonClick()
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
            doAsync {
                val db = AppDatabase.get(applicationContext)
                marketplaceBook = db.marketplaceBookDao().getMarketplaceBook(bookId)
                val collectionBook = marketplaceBook.collectionBook
                val saleBook = marketplaceBook.saleBook

                uiThread {
                    findViewById<TextView>(R.id.abb_edit_title).text = collectionBook.title
                    findViewById<TextView>(R.id.abb_edit_author).text = collectionBook.author
                    findViewById<TextView>(R.id.abb_edit_condition).text = saleBook.condition
                    findViewById<TextView>(R.id.abb_edit_price).text = saleBook.price.toString() + " €"
                    findViewById<TextView>(R.id.abb_edit_comment).text = saleBook.comment

                    findViewById<ImageView>(R.id.abb_image).setImageBitmap(
                        BookPictureLoader(applicationContext).load(collectionBook)
                    )
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

    private fun onBuyButtonClick() {
        val abbBuy = findViewById<Button>(R.id.abb_buy)
        abbBuy.setOnClickListener {
            // Add bought book to your own collection
            val ownedBook = marketplaceBook.collectionBook.copy()
            ownedBook.ownerId = Session(applicationContext).getLoggedInUser()!!.id!!
            ownedBook.saleBookId = null

            val db = AppDatabase.get(applicationContext)
            db.collectionBookDao().insert(ownedBook)

            //1. goto order placed
            val intent = Intent(applicationContext, OrderSuccessActivity::class.java )
            startActivity(intent)
        }
    }
}
