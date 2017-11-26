package red.padraig.alarmapp.notifications

import android.app.NotificationManager
import android.content.Context

interface NotificationController {

    fun update(id: Int, icon: Int, title: String, message: String)

    fun cancel(id: Int)

    class Impl(context: Context) : NotificationController {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmNotificationFactory = AlarmNotificationFactory(context)

        override fun update(id: Int, icon: Int, title: String, message: String) {
            notificationManager.notify(id, alarmNotificationFactory.create(icon, title, message))
        }

        override fun cancel(id: Int) {
            notificationManager.cancel(id)
        }

    }
}