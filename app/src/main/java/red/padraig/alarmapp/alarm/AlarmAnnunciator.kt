package red.padraig.alarmapp.alarm

import android.content.Context
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast

interface AlarmAnnunciator {

    fun play()

    fun stop()

    class Impl(context: Context): AlarmAnnunciator {

        private val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        private val ringtone: Ringtone = RingtoneManager.getRingtone(context, notification)

        init {
            ringtone.audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
        }

        override fun play() {
            ringtone.play()
        }

        override fun stop() {
            ringtone.stop()
        }
    }

    class ToastAlarm(context: Context): AlarmAnnunciator {

        private val toast = Toast.makeText(context, "Alarm!!!!", Toast.LENGTH_LONG)
        override fun play() {
            toast.show()
        }

        override fun stop() {
            toast.cancel()
        }

    }
}