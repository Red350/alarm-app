package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm_ringing.*
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.AlarmAnnunciator

class AlarmRingingActivity : BaseActivity() {

    lateinit var alarmAnnunciator: AlarmAnnunciator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ringing)

        alarmAnnunciator = AlarmAnnunciator.ToastAlarm(applicationContext)
        alarmAnnunciator.play()
    }

    override fun onPause() {
        super.onPause()
        alarmAnnunciator.stop()
    }

    override fun initialiseListeners() {
        button_alarmringing_stop.setOnClickListener { stopAlarm() }
        button_alarmringing_snooze.setOnClickListener { snoozeAlarm() }
    }

    override fun clearListeners() {
        button_alarmringing_stop.setOnClickListener(null)
        button_alarmringing_snooze.setOnClickListener(null)
    }

    private fun stopAlarm() {
        alarmAnnunciator.stop()
        setNextAlarm()
    }

    private fun snoozeAlarm() {
        // TODO: also have to set some global state to stop other emitted alarms from overriding the snoozed alarm
        // Register an alarm for 10 minutes from this point
    }

}
