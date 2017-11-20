package red.padraig.alarmapp.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import red.padraig.alarmapp.ui.activities.AlarmRingingActivity
import java.text.DateFormat
import java.util.*

class AlarmBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Acquire a wake lock to allow the alarm to wake the phone
        val powerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "AlarmWakelock")
        wakeLock.acquire(5000)

        Log.d("AlarmBroadcastReceiver", "Received broadcast at ${DateFormat.getDateTimeInstance().format(Date())}")
        val newIntent = Intent(context, AlarmRingingActivity::class.java)
        context.startActivity(newIntent)

        wakeLock.release()
    }
}