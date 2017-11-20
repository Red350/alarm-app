package red.padraig.alarmapp.alarm

import android.os.Parcel
import android.os.Parcelable
import red.padraig.alarmapp.util.getTodaysIndex
import java.util.*
import java.util.concurrent.TimeUnit

data class Alarm(var id: Long, val time: Long, val days: BooleanArray, val active: Boolean) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.createBooleanArray(),
            parcel.readByte() != 0.toByte())

    fun getNextTriggerTime(): Long {
        val todaysIndex = getTodaysIndex()    // Find first day after or including today that the alarm is set
        val timeSinceMidnight = timeSinceMidnight()

        // If alarm is set for today, still have to ensure the time hasn't already passed
        // The time is offset by 1 second to prevent any possibility of the triggered alarm being re-registered
        if (days[todaysIndex] and (time - TimeUnit.SECONDS.toMillis(1) > timeSinceMidnight)) {
            return alarmTimestampWithOffset(0)
        }

        // Check the next 7 days.
        // This ensures that if the next alarm is for the same day as today, but the time is
        // already passed, it will still be set for that day next week.
        (1..7)
                .first { days[(it + todaysIndex) % 7] }
                .let { return alarmTimestampWithOffset(it) }
    }

    // https://stackoverflow.com/a/34431277
    private fun timeSinceMidnight(): Long {
        val now = Calendar.getInstance()

        return now.timeInMillis - previousMidnightTimestamp()
    }

    private fun previousMidnightTimestamp(): Long {
        val midnight = Calendar.getInstance()

        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 0)
        midnight.set(Calendar.MILLISECOND, 0)

        return midnight.timeInMillis
    }

    private fun alarmTimestampWithOffset(daysOffset: Int): Long {
        return previousMidnightTimestamp() + TimeUnit.DAYS.toMillis(daysOffset.toLong()) + time
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(time)
        parcel.writeBooleanArray(days)
        parcel.writeByte(if (active) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alarm> {
        override fun createFromParcel(parcel: Parcel): Alarm {
            return Alarm(parcel)
        }

        override fun newArray(size: Int): Array<Alarm?> {
            return arrayOfNulls(size)
        }
    }

}