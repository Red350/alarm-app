package red.padraig.alarmapp.ui.activities

import android.os.Bundle
import red.padraig.alarmapp.R

class AlarmRingingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ringing)
    }

    override fun initialiseListeners() {
    }

    override fun clearListeners() {
    }
}
