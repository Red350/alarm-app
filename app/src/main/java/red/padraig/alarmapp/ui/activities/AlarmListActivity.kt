package red.padraig.alarmapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_alarm_list.*
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.Alarm
import red.padraig.alarmapp.callbacks.DeleteAlarmCallback
import red.padraig.alarmapp.callbacks.UpdateAlarmStateCallback
import red.padraig.alarmapp.ui.adapters.AlarmAdapter

class AlarmListActivity : BaseActivity(), UpdateAlarmStateCallback, DeleteAlarmCallback {

    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)
    }

    override fun onResume() {
        super.onResume()
        initialiseListView()
    }

    override fun initialiseListeners() {
        listview.setOnItemClickListener { _, _, position, _ ->
            launchEditAlarmActivity(alarmAdapter.alarms[position])
        }
    }

    override fun clearListeners() {
        listview.onItemClickListener = null
    }

    override fun initialiseSubscriptions() {
        super.initialiseSubscriptions()
        disposables.addAll(
                alarmDAO.updatedAlarms.subscribe(this::onAlarmUpdated),
                alarmDAO.deletedAlarmIds.subscribe(this::onAlarmDeleted)
        )
    }

    private fun initialiseListView() {
        alarmAdapter = AlarmAdapter(applicationContext, R.layout.alarmrow, alarmDAO.getAlarms(), this, this)
        listview.adapter = alarmAdapter
        listview.emptyView = text_alarmlist_empty
    }

    private fun launchEditAlarmActivity(alarm: Alarm) {
        val intent = Intent(this, SetAlarmActivity::class.java)
        intent.putExtra("alarm", alarm)
        startActivity(intent)
    }

    private fun onAlarmUpdated(alarm: Alarm) = alarmAdapter.updateRow(alarm)

    // Animates the deleted row before updating the list view
    private fun onAlarmDeleted(alarmId: Long) {
        var childIndex = -1

        // Reference: https://stackoverflow.com/a/6857762 (used animationListener instead of handler as suggested in the comments)
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        animation.duration = 250

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(anim: Animation?) {
            }

            override fun onAnimationEnd(anim: Animation?) {
                alarmAdapter.deleteRow(childIndex)
                listview.isEnabled = true   // Re-enable list view
            }

            override fun onAnimationStart(anim: Animation?) {
            }
        })
        // Reference Complete

        // Find the row index of the view that's being deleted
        for ((id) in alarmAdapter.alarms) {
            childIndex++
            if (id == alarmId) {
                break
            }
        }
        listview.isEnabled = false    // Disable list view during animation
        listview.getChildAt(childIndex).startAnimation(animation)
    }

    override fun updateAlarmState(id: Long, active: Boolean) {
        alarmDAO.updateAlarmState(id, active)
    }

    override fun deleteAlarm(id: Long) {
        alarmDAO.deleteAlarm(id)
    }

}

