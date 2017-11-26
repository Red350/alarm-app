package red.padraig.alarmapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import red.padraig.alarmapp.Extensions.fromEpochToDateString
import red.padraig.alarmapp.Extensions.fromEpochToTimeString
import red.padraig.alarmapp.R
import red.padraig.alarmapp.weather.LocationUpdater

class MainActivity : BaseActivity() {

    private lateinit var locationUpdater: LocationUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request location permissions, used for local weather data.
        requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
        )

        initialiseLocationUpdater()
    }

    override fun onResume() {
        super.onResume()
        // Set visibility of snooze button based on snooze state
        button_main_cancelsnooze.visibility = if (sharedPrefs.getSnoozeState()) View.VISIBLE else View.INVISIBLE
        displayNextAlarm(if (sharedPrefs.getSnoozeState()) sharedPrefs.getSnoozeTime() else sharedPrefs.getNextAlarmTime())
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmDAO.close()
    }

    override fun initialiseListeners() {
        button_main_viewalarms.setOnClickListener { startActivity(Intent(this, AlarmListActivity::class.java)) }
        button_main_setalarm.setOnClickListener { startActivity(Intent(this, SetAlarmActivity::class.java)) }
        button_main_cancelsnooze.setOnClickListener {
            cancelSnooze()
            displayNextAlarm(sharedPrefs.getNextAlarmTime())
            button_main_cancelsnooze.visibility = View.INVISIBLE
        }
        button_main_testalarm.setOnClickListener { startActivity(Intent(this, AlarmRingingActivity::class.java)) }
    }

    override fun clearListeners() {
        button_main_viewalarms.setOnClickListener(null)
        button_main_setalarm.setOnClickListener(null)
        button_main_cancelsnooze.setOnClickListener(null)
        button_main_testalarm.setOnClickListener(null)
    }

    private fun initialiseLocationUpdater() {
        if (checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission( android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            locationUpdater = LocationUpdater(sharedPrefs)
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,   // update every second (this is to ensure we get a response asap after subscribing)
                            // The 500m distance should prevent it from calling back too often and burning battery
                    500.0f, // 500 change
                    locationUpdater
            )
        }
    }

    // Displays the next alarm time, or a message if no alarms are set
    private fun displayNextAlarm(time: Long) {
        if (time == -1L) {
            text_main_title.text = getString(R.string.no_alarms_set)
            text_main_nextalarmtime.text = ""
            text_main_nextalarmdate.text = ""
        } else {
            text_main_title.text = getString(R.string.main_nextalarmtitle)
            text_main_nextalarmtime.text = time.fromEpochToTimeString()
            text_main_nextalarmdate.text = time.fromEpochToDateString()
        }
    }
}
