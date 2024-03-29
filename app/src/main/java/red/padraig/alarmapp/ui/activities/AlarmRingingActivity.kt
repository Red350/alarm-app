package red.padraig.alarmapp.ui.activities

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_alarm_ringing.*
import red.padraig.alarmapp.Extensions.fromEpochToDateTimeString
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.AlarmAnnunciator
import red.padraig.alarmapp.weather.DownloadWeather
import java.util.concurrent.TimeUnit


class AlarmRingingActivity : BaseActivity(), DownloadWeather.Callback {

    private val snoozeTimes: Array<Int> = arrayOf(1, 5, 10, 15)

    lateinit var alarmAnnunciator: AlarmAnnunciator
    var alarmSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ringing)

        // These flags are required for the alarm to trigger while phone is locked, or the screen is off
        // Also keeps the phone screen on to prevent the alarm ending prematurely
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        loadWeatherIcon()
        initialiseSnoozeSpinner()

        alarmAnnunciator = AlarmAnnunciator.Impl(applicationContext)

        // Register the next alarm and end the snooze state.
        // This prevents a situation where no alarm is set due to the user quitting this activity
        // without either snoozing or cancelling the alarm.
        sharedPrefs.setSnoozeState(false)
        setNextAlarm()
    }

    override fun onResume() {
        super.onResume()

        alarmAnnunciator.play()

        // Set the default snooze time to 10
        spinner_alarmringing_snoozetime.setSelection(2)
    }

    override fun onPause() {
        super.onPause()

        alarmAnnunciator.stop()
    }

    override fun initialiseListeners() {
        button_alarmringing_stop.setOnClickListener {
            stopAlarm()
            finish()
        }
        button_alarmringing_snooze.setOnClickListener {
            snoozeAlarm()
            finish()
        }
        spinner_alarmringing_snoozetime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val snoozeTime = parent?.getItemAtPosition(position) as Int
                sharedPrefs.setSnoozeDuration(snoozeTime)
            }
        }
    }

    override fun clearListeners() {
        button_alarmringing_stop.setOnClickListener(null)
        button_alarmringing_snooze.setOnClickListener(null)
        spinner_alarmringing_snoozetime.onItemSelectedListener = null
    }

    // Callback from weather download completing
    override fun onWeatherDownloaded(weatherData: Pair<String, Bitmap?>) {
        if (weatherData.second == null) {
            displayNoWeatherData()
        } else {
            // Hide loading spinner
            progress_alarmringing_weather.visibility = View.GONE

            // Display weather
            text_alarmringing_temperature.visibility = View.VISIBLE
            text_alarmringing_temperature.text = weatherData.first

            image_alarmringing_weather.visibility = View.VISIBLE
            image_alarmringing_weather.setImageBitmap(weatherData.second)
        }
    }

    private fun initialiseSnoozeSpinner() {
        val adapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item, snoozeTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_alarmringing_snoozetime.adapter = adapter
    }

    // Set the weather icon. Displays "weather not found" if no network connection.
    private fun loadWeatherIcon() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        // Need network access to get weather data
        if (networkInfo != null && networkInfo.isConnected) {
            DownloadWeather(this, sharedPrefs.getCoordinates()).execute()
        } else {
            displayNoWeatherData()
        }
    }

    private fun displayNoWeatherData() {
        progress_alarmringing_weather.visibility = View.GONE
        text_alarmringing_noweather.visibility = View.VISIBLE
    }

    // Stop the current alarm ringing and register the next alarm
    private fun stopAlarm() {
        alarmAnnunciator.stop()
        sharedPrefs.setSnoozeState(false)   // Must set snooze state false before registering a new alarm
        setNextAlarm()
        alarmSet = true
        Toast.makeText(
                this,
                getString(R.string.alarm_stopped_message),
                Toast.LENGTH_LONG
        ).show()
    }

    // Stop the current alarm ringing and register a snooze alarm based on the value in the spinner
    private fun snoozeAlarm() {
        alarmAnnunciator.stop()
        snoozeFor(sharedPrefs.getSnoozeDuration())
        sharedPrefs.setSnoozeState(true)
        alarmSet = true
        Toast.makeText(
                this,
                "Alarm snoozed for ${sharedPrefs.getSnoozeDuration()} minute(s)",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun snoozeFor(minutes: Int) {
        val snoozeTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes.toLong())
        alarmBroadcastSetter.set(applicationContext, snoozeTime)
        updateNotification(getString(R.string.alarm_snoozed_until_message), snoozeTime.fromEpochToDateTimeString())
        sharedPrefs.setSnoozeTime(snoozeTime)
    }
}
