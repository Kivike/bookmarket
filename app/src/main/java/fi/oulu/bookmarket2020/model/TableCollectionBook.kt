package fi.oulu.bookmarket2020.model

import androidx.room.*
import java.sql.Date

@Entity(tableName = "collection_book")
data class CollectionBook(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "isbn") var isbn: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "author") var author: String
)