package fi.oulu.bookmarket2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import fi.oulu.bookmarket2020.model.AppDatabase
import fi.oulu.bookmarket2020.model.CollectionBook
import fi.oulu.bookmarket2020.model.SaleBook
import org.jetbrains.anko.doAsync

class SellBookActivity : AppCompatActivity() {

    lateinit var collectionBook: CollectionBook

    lateinit var priceInput: EditText
    lateinit var conditionInput: EditText
    lateinit var commentInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_book)

        configureToolbar()
        setBookInfo()
        initForm()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.title_sell_book)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setBookInfo() {
        val bookId = intent.getIntExtra("bookId", 0)

        doAsync {
            val db = AppDatabase.get(applicationContext)
            collectionBook = db.collectionBookDao().getCollectionBook(bookId)

            findViewById<TextView>(R.id.book_title).text = collectionBook.title
            findViewById<TextView>(R.id.book_author).text = collectionBook.author
            findViewById<TextView>(R.id.book_published).text = collectionBook.publishYear.toString()
        }
    }

    private fun initForm()
    {
        val submitButton = findViewById<Button>(R.id.btn_submit)

        priceInput = findViewById(R.id.field_price)
        conditionInput = findViewById(R.id.field_condition)
        commentInput = findViewById(R.id.field_comments)

        submitButton.setOnClickListener {
            ///TODO validate price string
            val price = findViewById<EditText>(R.id.field_price).text.toString().toFloat()

            val saleBook = SaleBook(
                uid = null,
                collectionBookId = collectionBook.uid!!,
                price = price,
                comment = findViewById<EditText>(R.id.field_comments).toString()
            )

            doAsync {
                val db = AppDatabase.get(applicationContext)
                db.saleBookDao().insert(saleBook)

                val intent = Intent(applicationContext, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
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
}
