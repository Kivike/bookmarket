package fi.oulu.bookmarket2020.model

import androidx.room.*
import java.sql.Date

@Entity(tableName = "collection_book")
data class CollectionBook(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "isbn") var isbn: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "is_read") var isRead: Boolean,
    @ColumnInfo(name = "picture_path") var picturePath: String?
)