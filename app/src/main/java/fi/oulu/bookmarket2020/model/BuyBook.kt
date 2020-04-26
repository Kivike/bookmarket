package fi.oulu.bookmarket2020.model

import androidx.room.*

@Entity(
    tableName = "buy_book",
    indices = [Index(value = ["buyBookId"], unique = true)]
)
data class BuyBook(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "buyBookId") var buyBookId: Int,
    @ColumnInfo(name = "price") var price: Float,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "condition") var condition: String,
    @ColumnInfo(name = "comment") var comment: String
)

@Dao
interface BuyBookDao {
    @Transaction
    @Insert
    fun insert(book: BuyBook): Long

    @Query("SELECT * FROM buy_book")
    fun getbuyBooks(): List<BuyBook>

    @Query("DELETE FROM buy_book WHERE uid = :bookId")
    fun delete(bookId: Int)
}