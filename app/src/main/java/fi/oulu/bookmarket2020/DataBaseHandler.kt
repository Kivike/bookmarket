package fi.oulu.bookmarket2020

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import kotlinx.android.synthetic.main.collection_list_item.view.*
import java.util.*

val DATABASE_NAME = "Bookmarket_DB"
val TABLE_NAME = "Bookmarket"
val COL_ID = "id"
val COL_PERSON_NAME = "person"
val COL_BOOK_NAME = "book_name"
val COL_BOOK_SALEABLE ="book_saleable"
val COL_BOOK_READ = "book_already_read"
val COL_BOOK_CONDITION = "book_condition"
val COL_ISBN_NUMBER = "isbn number"
val COL_PRICE = "price"
val COL_COMMENTS = "comments"
val COL_EDITION = "edition"
val COL_AUTHOR = "author"
val COL_FIRST_NAME = "first name"
val COL_LAST_NAME = "last name"
val COL_EMAIL_ADDRESS = "email address"
val COL_PHONE = "phone"
val COL_PASSWORD = "password"


class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRST_NAME + "VARCHAR(256)"+
                COL_LAST_NAME + "VARCHAR(256)"+
                COL_EMAIL_ADDRESS + "VARCHAR(256)"+
                COL_PHONE + "INTEGER" +
                COL_PASSWORD + "VARCHAR(256)"+
                COL_BOOK_NAME + "VARCHAR(256)," +
                COL_AUTHOR + "VARCHAR(256)," +
                COL_ISBN_NUMBER + "INTEGER" +
                COL_EDITION + "INTEGER" +
                COL_PRICE + "INTEGER" +
                COL_COMMENTS + "VARCHAR(256)," +
                COL_BOOK_CONDITION + "VARCHAR(256)," +
                COL_BOOK_READ + "INTEGER DEFAULT 0," +
                COL_PERSON_NAME + "VARCHAR(256)," +
                COL_BOOK_SALEABLE + "INTEGER DEFAULT 0)";
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db?.execSQL("DROP TABLE IF EXITS" + TABLE_NAME)
        onCreate(db)
    }

    fun inserData(bookmarket : MarketplaceAdapter) {
        //Getting the Write Database
        //TODO: get the content values for needed content
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_BOOK_NAME, bookmarket.book_name)
        var result = db.insert(TABLE_NAME, null, cv)
        if (result == -1.toLong())
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
    }

    fun getData(bookmarket : SellBookActivity) {
        //TODO: Read database
        val db = this.readableDatabase
    }
}