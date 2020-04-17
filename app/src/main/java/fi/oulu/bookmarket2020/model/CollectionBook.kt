package fi.oulu.bookmarket2020.model

import androidx.room.*

@Entity(tableName = "collection_book")
data class CollectionBook(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "isbn") var isbn: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "publish_year") var publishYear: Int,
    @ColumnInfo(name = "is_read") var isRead: Boolean,
    @ColumnInfo(name = "picture_path") var picturePath: String?,
    @ColumnInfo(name = "sale_book_id") var saleBookId: Int?
)

@Dao
interface CollectionBookDao {
    @Transaction @Insert
    fun insert(book: CollectionBook): Long

    @Query("SELECT * FROM collection_book")
    fun getCollectionBooks(): List<CollectionBook>

    @Query("SELECT * FROM collection_book WHERE uid = :bookId")
    fun getCollectionBook(bookId: Int): CollectionBook

    @Query("DELETE FROM collection_book WHERE uid = :bookId")
    fun delete(bookId: Int)
}