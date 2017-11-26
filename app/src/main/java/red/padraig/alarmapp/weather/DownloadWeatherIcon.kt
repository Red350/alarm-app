package red.padraig.alarmapp.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


class DownloadWeatherIcon(imageView: ImageView, textView: TextView) : AsyncTask<Void, Void, Pair<String, Bitmap?>>() {

    // TODO: Use location instead of hardcoding to dublin
    private val TAG = "DownloadWeatherIcon"
    private val API_KEY = "2cf63b66a70d3a247eca2abb20d91634"
    private val WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?id=7778677&units=metric&APPID=" + API_KEY
    private val ICON_URL_BASE = "http://openweathermap.org/img/w/"

    private val imageViewReference: WeakReference<ImageView> = WeakReference(imageView)
    private val textViewReference: WeakReference<TextView> = WeakReference(textView)

    override fun doInBackground(vararg args: Void?): Pair<String, Bitmap?> {
        Log.d(TAG, "Starting connection...")
        try {
            if (API_KEY != "") {
                val weatherJson = getWeatherJson()
                return Pair(getFormattedTemperature(weatherJson), getWeatherIcon(weatherJson))
            } else {
                return Pair("", null)
            }
        } catch(e: Exception) {
            Log.d(TAG, "Failed to get weather info: " + e)
            return Pair("", null)
        }
    }

    override fun onPostExecute(result: Pair<String, Bitmap?>) {
        val textView = textViewReference.get()
        textView?.text = result.first

        val imageView = imageViewReference.get()
        imageView?.setImageBitmap(result.second)
    }

    private fun getWeatherJson(): JSONObject {
        val inputStream = getInputStreamFromUrl(WEATHER_URL)
        return JSONObject(inputStreamToString(inputStream))
    }

    private fun getFormattedTemperature(json: JSONObject): String {
        return json.getJSONObject("main").getString("temp") + 0x00B0.toChar() + "C"
    }

    private fun getWeatherIcon(json: JSONObject): Bitmap {
        val iconId = json.getJSONArray("weather").getJSONObject(0).getString("icon")
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
