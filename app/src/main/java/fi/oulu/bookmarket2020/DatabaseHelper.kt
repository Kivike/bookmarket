package fi.oulu.bookmarket2020

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import fi.oulu.bookmarket2020.model.User
import java.util.*


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Bookmarket_DB"
        private val TABLE_USER = "Bookmarket"
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PHONE = "user_phone"
        private val COLUMN_USER_PASSWORD = "user_password"

        // TODO: These column could be created for book sell and buy feature
        private val COLUMN_BOOK_NAME = "book_name"
        private val COLUMN_BOOK_SALEABLE ="book_saleable"
        private val COLUMN_BOOK_READ = "book_already_read"
        private val COLUMN_BOOK_CONDITION = "book_condition"
        private val COLUMN_ISBN_NUMBER = "isbn number"
        private val COLUMN_PRICE = "price"
        private val COLUMN_COMMENTS = "comments"
        private val COLUMN_EDITION = "edition"
        private val COLUMN_AUTHOR = "author"
    }

    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PHONE + " INTEGER,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")")
    // drop table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE)
        // Create tables again
        onCreate(db)

    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    fun getAllUser(): List<User> {

        // array of columns to fetch
        val columns =
            arrayOf(COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PHONE, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)
        // sorting orders
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = ArrayList<User>()
        val db = this.readableDatabase

        // query the user table
        val cursor = db.query(
            TABLE_USER, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder
        )         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                    phone = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)).toInt(),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD))
                )

                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }


    /**
     * This method is to create user record
     *
     * @param user
     */
    fun addUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PHONE, user.phone)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // Inserting Row
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    fun updateUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PHONE, user.phone)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // updating row
        db.update(
            TABLE_USER, values, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString())
        )
        db.close()
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    fun deleteUser(user: User) {

        val db = this.writableDatabase
        // delete user record by id
        db.delete(
            TABLE_USER, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString())
        )
        db.close()
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    fun checkUser(email: String, trim: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        // selection criteria
        val selection = "$COLUMN_USER_EMAIL = ?"

        // selection argument
        val selectionArgs = arrayOf(email)

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'manish@maharjan.com';
         */
        val cursor = db.query(
            TABLE_USER, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null
        )  //The sort order

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0) {
            return true
        }
        return false
    }
}
