package red.padraig.alarmapp.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import red.padraig.alarmapp.ui.activities.MainActivity

class AlarmNotificationFactory(val context: Context) {

    fun create(icon: Int, title: String, message: String): Notification {
        // TODO: Replace this with the new notification builder
        return Notification.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(createPendingIntent(context))
                .setOngoing(true)
                .build()
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }
}