package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm_list.*
import red.padraig.alarmapp.Alarm
import red.padraig.alarmapp.R
import red.padraig.alarmapp.ui.adapters.AlarmAdapter

class AlarmListActivity : BaseActivity() {

    lateinit var alarmList: Array<Alarm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)

        alarmList = alarmDAO.getAlarms().toTypedArray()
        listview.adapter = AlarmAdapter(alarmDAO, this, R.layout.alarmrow, this.alarmList)
    }

    override fun initialiseListeners() {
        listview.setOnItemClickListener { _, _, i, _ ->
            launchEditAlarmActivity(this.alarmList[i])
        }
    }

    override fun clearListeners() {
        listview.onItemClickListener = null
    }

    fun launchEditAlarmActivity(alarm: Alarm) {
        // todo: Launch SetAlarmActivity, with the alarm instance bundled
    }
}
