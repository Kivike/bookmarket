package fi.oulu.bookmarket2020

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import fi.oulu.bookmarket2020.model.CollectionBook
import kotlinx.android.synthetic.main.collection_list_item.view.*
import fi.oulu.bookmarket2020.model.AppDatabase
import kotlinx.android.synthetic.main.collection_list_item.view.book_author
import kotlinx.android.synthetic.main.collection_list_item.view.book_title
import org.jetbrains.anko.doAsync

class CollectionAdapter(
    private val applicationContext: Context,
    private val activityContext: Context,
    private val list: MutableList<CollectionBook>
) : BaseAdapter() {

    private val inflater: LayoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = convertView ?: inflater.inflate(R.layout.collection_list_item, parent, false)
        val book = getItem(position) as CollectionBook

        row!!.book_title.text = book.title
        row.book_author.text = book.author
        row.book_published.text = book.publishYear.toString()

        if (book.saleBookId == null) {
            row.sale_status.visibility = View.GONE
        }

        if (book.picturePath != null) {
            row.book_image.setImageBitmap(BookPictureLoader(applicationContext).load(book)!!)
        }

        initMenu(position, row, book)
        return row
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    private fun initMenu(position: Int, row: View, book: CollectionBook) {
        row.menu_button.setOnClickListener { button: View ->
            val popup = PopupMenu(activityContext, button)
            popup.menuInflater.inflate(R.menu.collection_item_menu, popup.menu)

            if (book.saleBookId != null) {
                popup.menu.findItem(R.id.sell).isVisible = false
            }
            popup.setOnMenuItemClickListener {
                    item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.sell -> {
                        startSellBookActivity(book)
                    }
                    R.id.delete -> {
                        deleteCollectionBook(book)
                        list.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
                true
            }
            popup.show()
        }
    }

    private fun startSellBookActivity(book: CollectionBook) {
        val intent = Intent(applicationContext, SellBookActivity::class.java)
        intent.putExtra("bookId", book.id)
        activityContext.startActivity(intent)
    }

    private fun deleteCollectionBook(book: CollectionBook) {
        doAsync {
            val db = AppDatabase.get(applicationContext)
            db.collectionBookDao().delete(book.id!!)
        }
        Toast.makeText(activityContext, "Deleted book " + book.title, Toast.LENGTH_SHORT).show()
    }
}