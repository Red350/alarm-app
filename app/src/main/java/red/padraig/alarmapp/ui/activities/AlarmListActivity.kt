package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm_list.*
import red.padraig.alarmapp.Alarm
import red.padraig.alarmapp.R
import red.padraig.alarmapp.ui.adapters.AlarmAdapter

class AlarmListActivity : BaseActivity() {

    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)

        alarmAdapter = AlarmAdapter(alarmDAO, this, R.layout.alarmrow, alarmDAO.getAlarms())
        listview.adapter = alarmAdapter
    }

    override fun initialiseListeners() {
        listview.setOnItemClickListener { _, _, i, _ ->
            // TODO: this no longer works as there's no local copy of alarms, can we still get alarms from the adapterview?
            // otherwise just reintroduce the local copy
            //launchEditAlarmActivity(this.alarms[i])
        }
    }

    override fun clearListeners() {
        listview.onItemClickListener = null
    }

    override fun initialiseSubscriptions() {
        disposables.addAll(
                alarmDAO.updatedAlarms.subscribe(this::alarmUpdated),
                alarmDAO.deletedAlarmIds.subscribe(this::alarmDeleted)
        )
    }

    fun launchEditAlarmActivity(alarm: Alarm) {
        // todo: Launch SetAlarmActivity, with the alarm instance bundled
    }

    private fun alarmUpdated(alarm: Alarm) = updateUi()

    private fun alarmDeleted(alarmId: Long) = updateUi()

    // TODO: Now that alarms are in a list, can instead sort the list and change individual alarm instance, rather than pulling all the alarms again
    // Updates the list adapter
    private fun updateUi() = alarmAdapter.updateView(alarmDAO.getAlarms())

}
