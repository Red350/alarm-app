package red.padraig.alarmapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import red.padraig.alarmapp.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        // Set visibility of snooze button based on snooze state
        button_main_cancelsnooze.visibility = if (getSnoozeState()) View.VISIBLE else View.INVISIBLE
        // TODO: Set the next alarm text field
    }

    override fun initialiseListeners() {
        button_main_viewalarms.setOnClickListener { startActivity(Intent(this, AlarmListActivity::class.java)) }
        button_main_setalarm.setOnClickListener { startActivity(Intent(this, SetAlarmActivity::class.java)) }
        button_main_cancelsnooze.setOnClickListener {
            cancelSnooze()
            button_main_cancelsnooze.visibility = View.INVISIBLE
        }
    }

    override fun clearListeners() {
        button_main_viewalarms.setOnClickListener(null)
        button_main_setalarm.setOnClickListener(null)
        button_main_cancelsnooze.setOnClickListener(null)
    }

}
