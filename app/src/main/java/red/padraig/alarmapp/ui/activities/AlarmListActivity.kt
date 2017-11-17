package red.padraig.alarmapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_alarm_list.*
import red.padraig.alarmapp.Alarm
import red.padraig.alarmapp.R
import red.padraig.alarmapp.ui.adapters.AlarmAdapter

class AlarmListActivity : BaseActivity() {

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
        disposables.addAll(
                alarmDAO.updatedAlarms.subscribe(this::alarmUpdated),
                alarmDAO.deletedAlarmIds.subscribe(this::alarmDeleted)
        )
    }

    private fun initialiseListView() {
        alarmAdapter = AlarmAdapter(alarmDAO, this, R.layout.alarmrow, alarmDAO.getAlarms())
        listview.adapter = alarmAdapter
        listview.emptyView = text_alarmlist_empty
    }

    private fun launchEditAlarmActivity(alarm: Alarm) {
        val intent = Intent(this, SetAlarmActivity::class.java)
        intent.putExtra("alarm", alarm)
        startActivity(intent)
        // todo: Launch SetAlarmActivity, with the alarm instance bundled
    }

    private fun alarmUpdated(alarm: Alarm) = updateUi()

    // Animates the deleted row before updating the list view
    private fun alarmDeleted(alarmId: Long) {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        animation.duration = 500
        animation.setAnimationListener( object: Animation.AnimationListener {
            override fun onAnimationRepeat(anim: Animation?) {
            }

            override fun onAnimationEnd(anim: Animation?) {
                updateUi()
                initialiseListeners()   // Re-enable list view onClick
            }

            override fun onAnimationStart(anim: Animation?) {
            }

        })

        // Find the row index of the view that's being deleted
        var childIndex = -1
        for ((id) in alarmAdapter.alarms) {
            childIndex++
            if (id == alarmId) {
                break
            }
        }
        clearListeners()    // Disable list view onClick during animation
        listview.getChildAt(childIndex).startAnimation(animation)
    }

    // TODO: Now that alarms are in a list, can instead sort the list and change individual alarm instance, rather than pulling all the alarms again
    // Updates the list adapter
    private fun updateUi() = alarmAdapter.updateView(alarmDAO.getAlarms())

}
