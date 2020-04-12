package fi.oulu.bookmarket2020.bookSearch

import android.os.AsyncTask
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.books.Books
import com.google.api.services.books.model.Volume;
import fi.oulu.bookmarket2020.BuildConfig
import java.io.IOException

class SearchTask: AsyncTask<String, Void, Volume>() {

    lateinit var listener: SearchListener

    override fun doInBackground(vararg params: String?): Volume? {
        //val isbn = params[0]
        val isbn = "9780552163361"
        //val isbn = "978-0-575-08892-4".replace("-", "")
        val query = "isbn:$isbn"

        val books = Books.Builder(
            AndroidHttp.newCompatibleTransport(),
            AndroidJsonFactory.getDefaultInstance(),
            null
        ).setApplicationName(
            BuildConfig.APPLICATION_ID
        ).build()

        return try {
            val list = books.volumes().list(query).setProjection("LITE")
            val result = list.execute()
            val items = result.items
            items?.first()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Volume?) {
        super.onPostExecute(result)
        listener.onResult(result)
    }
}