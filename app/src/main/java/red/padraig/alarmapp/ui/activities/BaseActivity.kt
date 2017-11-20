package red.padraig.alarmapp.ui.activities

import android.app.Activity
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import red.padraig.alarmapp.SharedPreferencesWrapper
import red.padraig.alarmapp.alarm.AlarmBroadcastSetter
import red.padraig.alarmapp.database.dao.AlarmDAO

// Base activity that all other activities inherit from
// Allows easier listener subscription and database control
abstract class BaseActivity : Activity() {

    protected val TAG = this::class.simpleName+"TAG"

    protected val disposables = CompositeDisposable()
    protected lateinit var alarmDAO: AlarmDAO
    protected lateinit var alarmBroadcastSetter: AlarmBroadcastSetter
    protected lateinit var sharedPrefs: SharedPreferencesWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmDAO = AlarmDAO(applicationContext)
        alarmBroadcastSetter = AlarmBroadcastSetter.Impl()
        sharedPrefs = SharedPreferencesWrapper(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        initialiseListeners()
        initialiseSubscriptions()
    }

    override fun onPause() {
        super.onPause()
        clearListeners()
        disposables.clear() // Clear all of the subscribed disposables
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: this may cause problems with one activity closing the connection after another has opened it
        alarmDAO.close()
    }

    open protected fun initialiseSubscriptions() {
        // TODO: at the moment, every time an alarm is changed or deleted, a new alarm is registered with the OS
        // The deleted/changed alarm may not actually change the next alarm.
        // Maybe solve this by keeping track of the next alarm time and id in shared prefs
        // If the deleted alarm matches the id, then set a new alarm
        // If the updated alarm has a trigger time that's sooner than shared prefs then register that alarm
        alarmDAO.updatedAlarms.subscribe { _ -> setNextAlarm()}
        alarmDAO.deletedAlarmIds.subscribe { _ -> setNextAlarm() }
    }

    protected fun setNextAlarm() {
        // Don't set any alarms if there's currently a snooze alarm set
        if (sharedPrefs.getSnoozeState()) return

        val nextAlarmTime = getNextAlarmTime()
        if (nextAlarmTime == -1L) {
            cancelAlarm()
        } else {
            alarmBroadcastSetter.set(this, nextAlarmTime)
        }
    }

    // If there are no alarms to be set, this cancels the alarm broadcast
    private fun cancelAlarm() {
        alarmBroadcastSetter.cancel(this)
    }

    private fun getNextAlarmTime(): Long {
        val alarms = alarmDAO.getAlarms()

        return alarms
                .filter { it.active }   // Only consider active alarms
                .map { it.getNextTriggerTime() }    // Map to the trigger time of each alarm
                .min()  // Get the minimum of the times
                .apply { sharedPrefs.setNextAlarmTime(this ?: -1L) }    // Store in sharedPrefs before returning
                ?: -1L
    }

    protected fun cancelSnooze() {
        sharedPrefs.setSnoozeState(false)
        setNextAlarm()
    }

    protected abstract fun initialiseListeners()

    protected abstract fun clearListeners()

}