package red.padraig.alarmapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import red.padraig.alarmapp.Extensions.fromEpochToDateString
import red.padraig.alarmapp.Extensions.fromEpochToTimeString
import red.padraig.alarmapp.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs.setSnoozeDuration(1)
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
    }

    override fun clearListeners() {
        button_main_viewalarms.setOnClickListener(null)
        button_main_setalarm.setOnClickListener(null)
        button_main_cancelsnooze.setOnClickListener(null)
    }

    private fun displayNextAlarm(time: Long) {
        text_main_nextalarmtime.text = if (time == -1L) getString(R.string.no_alarms_set) else time.fromEpochToTimeString()
        text_main_nextalarmdate.text = if (time == -1L) "" else time.fromEpochToDateString()
    }



}
