package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_set_alarm.*
import red.padraig.alarmapp.R

class SetAlarmActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)
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

    private fun setAlarm() {
        alarmDAO.insertAlarm(
                edittext_setalarm_time.text.toString().toInt(),
                edittext_setalarm_days.text.toString().toInt(),
                edittext_setalarm_active.text.toString() == "1"
        )
    }
}
