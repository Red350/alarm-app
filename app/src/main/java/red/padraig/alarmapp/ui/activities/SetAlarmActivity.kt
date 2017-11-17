package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_set_alarm.*
import red.padraig.alarmapp.Extensions.fromDaysArraytoBinary
import red.padraig.alarmapp.Extensions.fromHoursToMills
import red.padraig.alarmapp.Extensions.fromMinutesToMillis
import red.padraig.alarmapp.R

class SetAlarmActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)

        initialiseNumberPickers()
    }

    override fun initialiseListeners() {
        button_setalarm_set.setOnClickListener {
            setAlarm()
            onBackPressed()
        }
    }

    override fun clearListeners() {
        button_setalarm_set.setOnClickListener(null)
    }

    override fun initialiseSubscriptions() {

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

    private fun setAlarm() {
        val days = BooleanArray(7)
        days[0] = check_setalarm_monday.isChecked
        days[1] = check_setalarm_tuesday.isChecked
        days[2] = check_setalarm_wednesday.isChecked
        days[3] = check_setalarm_thursday.isChecked
        days[4] = check_setalarm_friday.isChecked
        days[5] = check_setalarm_saturday.isChecked
        days[6] = check_setalarm_sunday.isChecked

        alarmDAO.insertAlarm(
                picker_setalarm_hours.value.fromHoursToMills() + picker_setalarm_minutes.value.fromMinutesToMillis(),
                days.fromDaysArraytoBinary(),
                true
        )
    }
}
