package fi.oulu.bookmarket2020

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import fi.oulu.bookmarket2020.model.CollectionBook
import kotlinx.android.synthetic.main.collection_list_item.view.*

class MarketplaceAdapter(
    private val applicationContext: Context,
    private val activityContext: Context,
    private val list: MutableList<CollectionBook>
//    private val list: List<String>
) : BaseAdapter() {

    private val inflater: LayoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = inflater.inflate(R.layout.collection_list_item, parent, false)

        val book = getItem(position) as CollectionBook
        row.book_title.text = book.title
        row.book_author.text = book.author
        row.book_published.text = book.publishYear.toString()

        if (book.saleBookId == null) {
            row.sale_status.visibility = View.GONE
        }

        if (book.picturePath != null) {
            val pictureBitmap = BitmapFactory.decodeFile(book.picturePath)
            row.book_image.setImageBitmap(pictureBitmap)
        }

        row.menu_button.setOnClickListener {
            startBuyBookActivity(book)
        }

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


//    private fun initMenu(position: Int, row: View, book: CollectionBook) {
//        row.menu_button.setOnClickListener { button: View ->
//            val popup = PopupMenu(activityContext, button)
//            popup.menuInflater.inflate(R.menu.collection_item_menu, popup.menu)
//
//            popup.setOnMenuItemClickListener {
//                    item: MenuItem? ->
//                when (item!!.itemId) {
//                    R.id.sell -> {
//                        startBuyBookActivity(book)
//                    }
//                }
//                true
//            }
//            popup.show()
//        }
//    }

    private fun startBuyBookActivity(book: CollectionBook) {
        val intent = Intent(applicationContext, BuyBookActivity::class.java)
        intent.putExtra("book_id", book.uid)
        activityContext.startActivity(intent)
    }
}