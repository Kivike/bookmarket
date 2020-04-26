package fi.oulu.bookmarket2020.model

import androidx.room.*

@Entity(
    tableName = "sale_book",
    indices = [Index(value = ["collectionBookId"], unique = true)]
)
data class SaleBook(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "collectionBookId") var collectionBookId: Int,
    @ColumnInfo(name = "price") var price: Float,
//    @ColumnInfo(name = "condition") var condition: String,
    @ColumnInfo(name = "comment") var comment: String
)

@Dao
interface SaleBookDao {
    @Transaction
    @Insert
    fun insert(book: SaleBook): Long

    @Query("SELECT * FROM sale_book")
    fun getCollectionBooks(): List<SaleBook>

    @Query("DELETE FROM sale_book WHERE uid = :bookId")
    fun delete(bookId: Int)
}