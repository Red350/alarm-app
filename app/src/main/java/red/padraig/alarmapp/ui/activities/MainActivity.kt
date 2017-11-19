package red.padraig.alarmapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import red.padraig.alarmapp.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarms = alarmDAO.getAlarms()
        for(alarm in alarms) {
            Log.d(TAG, "Alarm is: " + alarm)
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: Set the next alarm text field
    }

    override fun initialiseListeners() {
        button_main_viewalarms.setOnClickListener { startActivity(Intent(this, AlarmListActivity::class.java))  }
        button_main_setalarm.setOnClickListener { startActivity(Intent(this, SetAlarmActivity::class.java)) }
    }

    override fun clearListeners() {
        button_main_viewalarms.setOnClickListener(null)
        button_main_setalarm.setOnClickListener(null)
    }

}
