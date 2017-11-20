package red.padraig.alarmapp.callbacks

interface UpdateAlarmStateCallback {
    fun updateAlarmState(id: Long, active: Boolean)
}