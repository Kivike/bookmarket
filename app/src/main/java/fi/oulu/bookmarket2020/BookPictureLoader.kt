package fi.oulu.bookmarket2020

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import fi.oulu.bookmarket2020.model.CollectionBook
import kotlinx.android.synthetic.main.collection_list_item.view.*
import java.net.URL

class BookPictureLoader(private val context: Context) {

    fun load(book: CollectionBook): Bitmap? {
        var pictureBitmap: Bitmap? = null

        if (book.picturePath != null) {
            val path = book.picturePath!!

            if (path.startsWith("http")) {
                // Does not work
                //val url = URL(path)
                //pictureBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } else if (path.startsWith("drawable://")) {
                val drawableId = path.replace("drawable://", "").toInt()
                pictureBitmap = BitmapFactory.decodeResource(context.resources, drawableId)
            } else {
                pictureBitmap = BitmapFactory.decodeFile(path)
            }
        }
        return pictureBitmap

    }
}