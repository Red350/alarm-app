package red.padraig.alarmapp.ui.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_alarm_ringing.*
import red.padraig.alarmapp.Extensions.fromEpochToDateTimeString
import red.padraig.alarmapp.Extensions.fromEpochToTimeString
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.AlarmAnnunciator
import red.padraig.alarmapp.weather.DownloadWeatherIcon
import java.util.concurrent.TimeUnit


class AlarmRingingActivity : BaseActivity() {

    private val snoozeTimes: Array<Int> = arrayOf(1, 5, 10, 15)

    lateinit var alarmAnnunciator: AlarmAnnunciator
    var alarmSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ringing)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        loadWeatherIcon()
        initialiseSnoozeSpinner()

        // TODO: Replace this with AlarmAnnunciator.Impl
        alarmAnnunciator = AlarmAnnunciator.ToastAlarm(applicationContext)
        alarmAnnunciator.play()

        // Register the next alarm and end the snooze state.
        // This prevents a situation where no alarm is set due to the user quitting this activity
        // without either snoozing or cancelling the alarm.
        sharedPrefs.setSnoozeState(false)
        setNextAlarm()
    }

    override fun onResume() {
        super.onResume()

        // TODO: Update the time every minute
        text_alarmringing_time.text = System.currentTimeMillis().fromEpochToTimeString()

        // Set the default snooze time to 10
        spinner_alarmringing_snoozetime.setSelection(2)
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

    private fun initialiseSnoozeSpinner() {
        val adapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item, snoozeTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_alarmringing_snoozetime.adapter = adapter
    }

    // Set the weather icon, if there's no network connection this does nothing
    private fun loadWeatherIcon() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            DownloadWeatherIcon(image_alarmringing_weather, text_alarmringing_temperature).execute()
        }
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
