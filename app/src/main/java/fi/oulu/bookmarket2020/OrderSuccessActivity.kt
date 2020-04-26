package fi.oulu.bookmarket2020

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrderSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_success)
        onOrderSuccess()
    }

    private fun onOrderSuccess() {
        val aos_button = findViewById<Button>(R.id.aos_button)
        aos_button.setOnClickListener{
            val intent = Intent(applicationContext, CollectionActivity::class.java)
            startActivity(intent)
        }
    }
/*
    private fun setBookInfo() {
        val bookId = intent.getIntExtra("bookId", 0)

        doAsync {
            val db = AppDatabase.get(applicationContext)
            collectionBook = db.collectionBookDao().getCollectionBook(bookId)

            findViewById<TextView>(R.id.book_title).text = collectionBook.title
            findViewById<TextView>(R.id.book_author).text = collectionBook.author
            findViewById<TextView>(R.id.book_published).text = collectionBook.publishYear.toString()
        }
    } */
}
