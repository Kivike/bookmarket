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
        onBuybuttonClick()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Buy book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun onBuybuttonClick() {
        val abb_buy = findViewById<Button>(R.id.abb_buy)
        abb_buy.setOnClickListener {
            //1. goto order placed
            val intent = Intent(applicationContext, OrderSuccessActivity::class.java )
            startActivity(intent)

            //2. store in db
        }
    }
}
