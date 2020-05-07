package fi.oulu.bookmarket2020

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import fi.oulu.bookmarket2020.model.CollectionBook
import kotlinx.android.synthetic.main.collection_list_item.view.*
import java.net.URL

class BookPictureLoader(private val context: Context) {

    enum class ImgSize(val width: Int) {
        LISTING(200),
        FULL(500)
    }

    fun load(book: CollectionBook, size: ImgSize = ImgSize.LISTING): Bitmap? {
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

        if (pictureBitmap != null) {
            pictureBitmap = scaleBitmap(pictureBitmap, size)
        }
        return pictureBitmap

    }

    private fun scaleBitmap(bitmap: Bitmap, size: ImgSize): Bitmap {
        val w = bitmap.width.toFloat()
        val h = bitmap.height.toFloat()
        val aspectRatio = w / h
        val scaledW = size.width.toFloat()
        val scaledH = scaledW / aspectRatio
        return Bitmap.createScaledBitmap(bitmap, scaledW.toInt(), scaledH.toInt(), false)
    }
}