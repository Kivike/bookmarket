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
}
