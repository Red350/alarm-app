package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_set_alarm.*
import red.padraig.alarmapp.Extensions.fromHoursToMills
import red.padraig.alarmapp.Extensions.fromMinutesToMillis
import red.padraig.alarmapp.Extensions.getHours
import red.padraig.alarmapp.Extensions.getMinutes
import red.padraig.alarmapp.R
import red.padraig.alarmapp.alarm.Alarm
import red.padraig.alarmapp.util.getTodaysIndex
import java.util.*

class SetAlarmActivity : BaseActivity() {

    // Allows one button to both create and update an alarm, depending on the context
    private lateinit var dbOperation: () -> Unit
    private var alarm: Alarm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)

        initialiseNumberPickers()

        // If the activity was started with a bundled alarm, load that into the view
        val alarm: Alarm? = intent.getParcelableExtra<Alarm>("alarm")
        if (alarm != null) {
            this.alarm = alarm
            initialiseScreenToUpdatingAlarm(alarm) // Load the bundled alarm into the view
            dbOperation = this::updateAlarm
        } else {
            initialiseScreenToCurrentDayAndTime()
            dbOperation = this::setAlarm
        }
    }

    override fun initialiseListeners() {
        button_setalarm_set.setOnClickListener {
            if (atLeastOneDayChecked()) {
                dbOperation()
                onBackPressed()
            } else {
                promptCheckDays()
            }
        }
    }

    override fun clearListeners() {
        button_setalarm_set.setOnClickListener(null)
    }

    private fun initialiseNumberPickers() {
        picker_setalarm_hours.minValue = 0
        picker_setalarm_hours.maxValue = 23
        picker_setalarm_minutes.minValue = 0
        picker_setalarm_minutes.maxValue = 59

        // Display single digits with a leading zero
        picker_setalarm_hours.setFormatter { num ->
            if (num < 10) "0" + num else num.toString()
        }
        picker_setalarm_minutes.setFormatter { num ->
            if (num < 10) "0" + num else num.toString()
        }
    }

    private fun initialiseScreenToCurrentDayAndTime() {
        // Default to having today ticked
        when (getTodaysIndex()) {
            0 -> check_setalarm_monday.isChecked = true
            1 -> check_setalarm_tuesday.isChecked = true
            2 -> check_setalarm_wednesday.isChecked = true
            3 -> check_setalarm_thursday.isChecked = true
            4 -> check_setalarm_friday.isChecked = true
            5 -> check_setalarm_saturday.isChecked = true
            6 -> check_setalarm_sunday.isChecked = true
        }

        val calendar = Calendar.getInstance()
        picker_setalarm_hours.value = calendar.get(Calendar.HOUR_OF_DAY)
        picker_setalarm_minutes.value = calendar.get(Calendar.MINUTE)
    }

    // Loads the bundled alarm instance into the view
    private fun initialiseScreenToUpdatingAlarm(alarm: Alarm) {
        picker_setalarm_hours.value = alarm.time.getHours().toInt()
        picker_setalarm_minutes.value = alarm.time.getMinutes().toInt()

        check_setalarm_monday.isChecked = alarm.days[0]
        check_setalarm_tuesday.isChecked = alarm.days[1]
        check_setalarm_wednesday.isChecked = alarm.days[2]
        check_setalarm_thursday.isChecked = alarm.days[3]
        check_setalarm_friday.isChecked = alarm.days[4]
        check_setalarm_saturday.isChecked = alarm.days[5]
        check_setalarm_sunday.isChecked = alarm.days[6]

        button_setalarm_set.text = getString(R.string.setalarm_updatebutton)
    }

    private fun setAlarm() {
        alarmDAO.createAlarm(getTime(), getCheckedDays(), true)
    }

    private fun updateAlarm() {
        if (alarm != null) {
            alarm?.let { alarmDAO.updateAlarm(it.id, getTime(), getCheckedDays(), it.active) }
        } else {
            throw RuntimeException("Unable to update alarm, no alarm instance stored")
        }
    }

    private fun atLeastOneDayChecked(): Boolean {
        return getCheckedDays().any { it }
    }

    private fun promptCheckDays() = Toast.makeText(this, "Select at least one day", Toast.LENGTH_SHORT).show()

    private fun getCheckedDays(): BooleanArray {
        val days = BooleanArray(7)
        days[0] = check_setalarm_monday.isChecked
        days[1] = check_setalarm_tuesday.isChecked
        days[2] = check_setalarm_wednesday.isChecked
        days[3] = check_setalarm_thursday.isChecked
        days[4] = check_setalarm_friday.isChecked
        days[5] = check_setalarm_saturday.isChecked
        days[6] = check_setalarm_sunday.isChecked

        return days
    }

    private fun getTime() = picker_setalarm_hours.value.fromHoursToMills().toLong() + picker_setalarm_minutes.value.fromMinutesToMillis().toLong()

}
