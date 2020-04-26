package fi.oulu.bookmarket2020

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.collection_list_item.view.*

class MarketplaceAdapter(context: Context, private val list: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = convertView ?: inflater.inflate(R.layout.collection_list_item, parent, false)

        row.book_title.text = list[position]
        row.book_author.text ="Firstname Lastname"
        row.sale_status.text = ""

        if (position % 4 != 0) {
            row.sale_status.text = position.toString() + " €"
            row.sale_status.textSize = 18.0f
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

}