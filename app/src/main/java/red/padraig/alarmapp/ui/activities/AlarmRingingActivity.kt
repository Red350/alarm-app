package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_alarm_ringing.*
import red.padraig.alarmapp.Extensions.fromEpochToDateTimeString
import red.padraig.alarmapp.Extensions.fromEpochToTimeString
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.AlarmAnnunciator
import red.padraig.alarmapp.weather.DownloadWeatherIcon
import java.util.concurrent.TimeUnit


class AlarmRingingActivity : BaseActivity() {

    lateinit var alarmAnnunciator: AlarmAnnunciator
    var alarmSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ringing)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        DownloadWeatherIcon(image_alarmringing_weather).execute()

        text_alarmringing_time.text = System.currentTimeMillis().fromEpochToTimeString()

        alarmAnnunciator = AlarmAnnunciator.ToastAlarm(applicationContext)
        alarmAnnunciator.play()
    }

    override fun onPause() {
        super.onPause()
        alarmAnnunciator.stop() // Alarm stops ringing if the user puts the app in the background

        // If the user backgrounds the activity without snoozing or cancelling, a snooze is set by default
        if (!alarmSet) snoozeAlarm()
        finish()
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
    }

    override fun clearListeners() {
        button_alarmringing_stop.setOnClickListener(null)
        button_alarmringing_snooze.setOnClickListener(null)
    }

    // Stop the current alarm ringing and register the next alarm
    private fun stopAlarm() {
        alarmAnnunciator.stop()
        sharedPrefs.setSnoozeState(false)   // Must set snooze state false before registering a new alarm
        setNextAlarm()
        alarmSet = true
    }

    // Stop the current alarm ringing and register a snooze alarm for 10 minutes from this point
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
        updateNotification( "Alarm snoozed until: ", snoozeTime.fromEpochToDateTimeString())
        sharedPrefs.setSnoozeTime(snoozeTime)
    }
}
