package fi.oulu.bookmarket2020.model

import androidx.room.*

data class MarketplaceBook(
    @Embedded val collectionBook: CollectionBook,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectionBookId"
    )
    val saleBook: SaleBook?
)

@Dao
interface MarketplaceBookDao {

    @Transaction
    @Query("SELECT * FROM collection_book WHERE owner_id != :currentUserId AND sale_book_id IS NOT NULL")
    fun getMarketplaceBooks(currentUserId: Int): List<MarketplaceBook>
}