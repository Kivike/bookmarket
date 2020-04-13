package fi.oulu.bookmarket2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@RegisterActivity
    private lateinit var register: ConstraintLayout
    private lateinit var edit_fn: ConstraintLayout
    private lateinit var edit_ln: ConstraintLayout
    private lateinit var edit_email: ConstraintLayout
    private lateinit var edit_ph: ConstraintLayout
    private lateinit var edit_pass: ConstraintLayout
    private lateinit var edit_new_pass: ConstraintLayout

    private lateinit var


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        configureToolbar()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Register")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun inputValidation() {

    }
}
