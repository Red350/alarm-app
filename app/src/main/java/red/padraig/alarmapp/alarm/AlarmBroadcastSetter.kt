package red.padraig.alarmapp.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import red.padraig.alarmapp.Extensions.fromEpochToDateString
import red.padraig.alarmapp.Extensions.fromEpochToDateTimeString

interface AlarmBroadcastSetter {

    companion object {
        private val TAG = "AlarmBroadcastSetter"
    }

    fun set(context: Context, time: Long)

    fun cancel(context: Context)

    class Impl: AlarmBroadcastSetter {

        override fun set(context: Context, time: Long) {
            getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, time, createPendingIntent(context))
            Log.d(TAG, "Alarm set for ${time.fromEpochToDateTimeString()}")
        }

        override fun cancel(context: Context) {
            getAlarmManager(context).cancel(createPendingIntent(context))
            Log.d(TAG, "No alarms set")
        }

        private fun createPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        private fun getAlarmManager(context: Context): AlarmManager {
            return context.getSystemService(AlarmManager::class.java)
        }
    }

    class TestBroadcastSetter : AlarmBroadcastSetter {
        override fun set(context: Context, time: Long) {
            Toast.makeText(context, "Test alarm set for ${time.fromEpochToDateString()}", Toast.LENGTH_SHORT).show()
        }

        override fun cancel(context: Context) {
            Toast.makeText(context, "Test alarm cancelled", Toast.LENGTH_SHORT).show()
        }

    }
}