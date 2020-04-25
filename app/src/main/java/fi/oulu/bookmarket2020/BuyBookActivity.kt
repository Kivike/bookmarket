package fi.oulu.bookmarket2020

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class BuyBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_book)

        configureToolbar()
        onBuybuttonClick89()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Buy book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    // When buy button is clicked
    /*
     * purchased books need to be visible in db
     * buy button should have on click listener and that should take to order placed activity
     *
     *
     *
        row.collection_list_items.setOnClickListener {
            startBuyBookActivity(book)
        }
     */

    private fun onBuybuttonClick89() {
        val abb_buy = findViewById<Button>(R.id.abb_buy)
        abb_buy.setOnClickListener {
            //1. goto order placed
            /**
             *         val intent = Intent(applicationContext, BuyBookActivity::class.java)
            intent.putExtra("book_id", book.uid)
            activityContext.startActivity(intent)
             */
            val intent = Intent(applicationContext, OrderSuccessActivity::class.java )
            startActivity(intent)

            //2. store in db
        }
    }
}
