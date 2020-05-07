package fi.oulu.bookmarket2020

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import fi.oulu.bookmarket2020.model.AppDatabase
import fi.oulu.bookmarket2020.model.CollectionBook
import fi.oulu.bookmarket2020.model.SaleBook
import fi.oulu.bookmarket2020.model.User
import org.jetbrains.anko.doAsync

class DemoSampleInstaller {

    fun install(applicationContext: Context)
    {
        doAsync {
            installDemoUsers(applicationContext)
            installDemoBooks(applicationContext)
        }
    }

    private fun installDemoUsers(applicationContext: Context)
    {
        val db = AppDatabase.get(applicationContext)
        val userDao = db.userDao()

        val users = arrayOf(
            User(
                id = 1000,
                name = "Demo user",
                email = "demo@mail.com",
                password = "123"
            ),
            User(
                id = 2000,
                name = "Charlie",
                email = "charlie@mail.com",
                password = "123"
            )
        )
        for (user in users) {
            userDao.deleteUser(user)
            db.collectionBookDao().deleteByOwner(user.id!!)

            try {
                userDao.addUser(user)
            } catch (e: SQLiteConstraintException) {
                userDao.updateUser(user)
            }
        }
    }

    private fun installDemoBooks(applicationContext: Context)
    {
        val books = arrayOf(
            CollectionBook(
                id = 1000,
                isbn = "1880418622",
                title = "The Dark Tower VII",
                author = "Stephen King",
                publishYear = 2004,
                isRead = true,
                picturePath = "drawable://" + R.drawable.darktower7,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1001,
                isbn = "1880418622",
                title = "Snuff",
                author = "Terry Pratchett",
                publishYear = 2004,
                isRead = true,
                picturePath = "drawable://" + R.drawable.snuff,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1002,
                isbn = "9788676661138",
                title = "Norwegian Wood",
                author = "Haruki Murakami",
                publishYear = 2001,
                isRead = true,
                picturePath = "drawable://" + R.drawable.norwegianwood,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1003,
                isbn = "9789512083954",
                title = "The Quantum Thief",
                author = "Hannu Rajaniemi",
                publishYear = 2010,
                isRead = true,
                picturePath = "drawable://" + R.drawable.kvanttivaras,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1004,
                isbn = "9510423874",
                title = "Jano",
                author = "Jo Nesbo ",
                publishYear = 2017,
                isRead = true,
                picturePath = "drawable://" + R.drawable.jano,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1005,
                isbn = "9780452284708",
                title = "The Dark Tower II: The Drawing of the Three",
                author = "Stephen King",
                publishYear = 1987,
                isRead = true,
                picturePath = "drawable://" + R.drawable.darktower2,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1006,
                isbn = "9780452279629",
                title = "The Dark Tower III: The Waste Lands",
                author = "Stephen King",
                publishYear = 1991,
                isRead = true,
                picturePath = "drawable://" + R.drawable.darktower3,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1007,
                isbn = "9780452279179",
                title = "The Dark Tower IV: Wizard and Glass",
                author = "Stephen King",
                publishYear = 1997,
                isRead = true,
                picturePath = "drawable://" + R.drawable.darktower4,
                saleBookId = null,
                ownerId = 1000
            ),
            CollectionBook(
                id = 1008,
                isbn = "9780340827154",
                title = "The Dark Tower V: Wolves of the Calla",
                author = "Stephen King",
                publishYear = 2003,
                isRead = true,
                picturePath = "drawable://" + R.drawable.darktower5,
                saleBookId = null,
                ownerId = 1000
            )
        )

        books.forEach {
            saveBook(applicationContext, it)
        }
    }

    private fun saveBook(applicationContext: Context, book: CollectionBook) {
        val bookDao = AppDatabase.get(applicationContext).collectionBookDao()
        val saleBookDao = AppDatabase.get(applicationContext).saleBookDao()

        /**
         * Save collection book
         */
        bookDao.delete(book.id!!)

        try {
            bookDao.insert(book)
        } catch (e:SQLiteConstraintException) {
            bookDao.update(book)
        }

        /**
         * Create sale book
         */
        val saleBookId = book.id!!.toInt()

        val saleBook = SaleBook(
            id = saleBookId,
            collectionBookId = saleBookId,
            price = ((saleBookId - 999) * 10).toFloat(),
            condition = "unused",
            comment = "First hardcover print of the book. Great book, you will love it!"
        )


        /**
         * Save sale book
         */
        saleBookDao.delete(saleBookId)

        try {
            saleBookDao.insert(saleBook)
        } catch (e:SQLiteConstraintException) {
            saleBookDao.update(saleBook)
        }

        book.saleBookId = saleBookId
        bookDao.update(book)
    }
}