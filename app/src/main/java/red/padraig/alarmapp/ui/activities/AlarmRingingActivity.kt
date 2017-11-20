package red.padraig.alarmapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_alarm_ringing.*
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.AlarmAnnunciator
import java.util.concurrent.TimeUnit


class AlarmRingingActivity : BaseActivity() {

    lateinit var alarmAnnunciator: AlarmAnnunciator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ringing)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        alarmAnnunciator = AlarmAnnunciator.ToastAlarm(applicationContext)
        alarmAnnunciator.play()
    }

    override fun onPause() {
        super.onPause()
        alarmAnnunciator.stop()
        // Default behaviour is to snooze the alarm if the activity somehow pauses before the user makes a choice
        // This also prevents a situation where no alarm is set at all
        snoozeAlarm()
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
        setSnoozeState(false)
        setNextAlarm()
    }

    // Register a snooze alarm for 10 minutes from this point
    private fun snoozeAlarm() {
        // TODO: also have to set some global state to stop other emitted alarms from overriding the snoozed alarm
        snoozeFor(10)
        setSnoozeState(true)
    }

    private fun snoozeFor(minutes: Int) {
        alarmBroadcastSetter.set(applicationContext, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10))
    }
}
