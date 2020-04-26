package fi.oulu.bookmarket2020.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CollectionBook::class, SaleBook::class, BuyBook::class, User::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionBookDao() : CollectionBookDao
    abstract fun saleBookDao() : SaleBookDao
    abstract fun buyBookDao() : BuyBookDao
    abstract fun userDao(): UserDao
    abstract fun marketplaceBookDao() : MarketplaceBookDao

    companion object {
        var db: AppDatabase? = null
        val DB_NAME = "app_db"

        fun get(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return db!!
        }
    }
}