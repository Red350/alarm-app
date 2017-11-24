package red.padraig.alarmapp.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


class DownloadWeatherIcon(imageView: ImageView) : AsyncTask<Void, Void, Bitmap>() {

    private val TAG = "DownloadWeatherIcon"
    private val API_KEY = ""
    private val WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?id=7778677&APPID=" + API_KEY
    private val ICON_URL_BASE = "http://openweathermap.org/img/w/"

    private val imageViewReference: WeakReference<ImageView> = WeakReference(imageView)

    override fun doInBackground(vararg args: Void?): Bitmap? {
        Log.d(TAG, "Starting connection...")
        return if (API_KEY != "") getIconImage(getWeatherIconId()) else null
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null) {
            val imageView = imageViewReference.get()
            imageView?.setImageBitmap(result)
        }
    }

    private fun getWeatherIconId(): String {
        Log.d(TAG, "Getting json from: " + WEATHER_URL)
        val inputStream = getInputStreamFromUrl(WEATHER_URL)
        return parseIcon(inputStream)
    }

    private fun parseIcon(inputStream: InputStream): String {
        val json = JSONObject(inputStreamToString(inputStream))
        Log.d(TAG, "JSON: " + json)
        val weatherArray = json.getJSONArray("weather")
        Log.d(TAG, "Weather array: " + weatherArray)
        val weather = weatherArray.getJSONObject(0)
        Log.d(TAG, "Weather object: " + weather)
        Log.d(TAG, "Icon: " + weather.getString("icon"))
        return weather.getString("icon")
    }

    private fun getIconImage(iconId: String): Bitmap {
        val url = ICON_URL_BASE + iconId + ".png"
        Log.d(TAG, "Getting icon from: " + url)
        val inputStream = getInputStreamFromUrl(url)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun getInputStreamFromUrl(url: String): InputStream {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.readTimeout = 10000
        conn.connectTimeout = 15000
        conn.requestMethod = "GET"
        conn.doInput = true

        conn.connect()
        conn.inputStream.toString()
        return conn.inputStream
    }

    private fun inputStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        reader.forEachLine {
            stringBuilder.append(it).append('\n')
        }
        inputStream.close()
        return stringBuilder.toString()
    }
}
