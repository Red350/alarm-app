package red.padraig.alarmapp

import android.content.Context
import android.content.SharedPreferences
import android.location.Location

class SharedPreferencesWrapper(val context: Context) {

    private val SHARED_PREFS_TAG = "alarmSharedPrefs"
    val SNOOZE_STATE = "snoozeState"
    val SNOOZE_TIME = "snoozeTime"
    val NEXT_ALARM_TIME = "nextAlarmTime"
    val SNOOZE_DURATION = "snoozeDuration"
    val LATITUDE = "lat"
    val LONGITUDE = "lon"

    companion object {
        val INVALID_TIME = -1L
        val DEFAULT_SNOOZE_DURATION = 10
    }

    fun setSnoozeState(state: Boolean) {
        getSharedPreferences().edit().putBoolean(SNOOZE_STATE, state).apply()
    }

    fun getSnoozeState(): Boolean {
        return getSharedPreferences().getBoolean(SNOOZE_STATE, false)
    }

    fun setSnoozeTime(time: Long) {
        getSharedPreferences().edit().putLong(SNOOZE_TIME, time).apply()
    }

    fun getSnoozeTime(): Long {
        return getSharedPreferences().getLong(SNOOZE_TIME, INVALID_TIME)
    }

    fun setNextAlarmTime(time: Long) {
        getSharedPreferences().edit().putLong(NEXT_ALARM_TIME, time).apply()
    }

    fun getNextAlarmTime(): Long {
        return getSharedPreferences().getLong(NEXT_ALARM_TIME, INVALID_TIME)
    }

    fun setSnoozeDuration(time: Int) {
        getSharedPreferences().edit().putInt(SNOOZE_DURATION, time).apply()
    }

    fun getSnoozeDuration(): Int {
        return getSharedPreferences().getInt(SNOOZE_DURATION, DEFAULT_SNOOZE_DURATION)
    }

    fun setCoordinates(location: Location) {
        val editor = getSharedPreferences().edit()
        editor.putFloat(LATITUDE, location.latitude.toFloat())
        editor.putFloat(LONGITUDE, location.longitude.toFloat())
        editor.apply()
    }

    fun getCoordinates(): Pair<Float, Float> {
        val sharedPreferences = getSharedPreferences()
        return Pair(
                sharedPreferences.getFloat(LATITUDE, 0f),
                sharedPreferences.getFloat(LONGITUDE, 0f)
        )
    }

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_TAG, Context.MODE_PRIVATE)
    }

}