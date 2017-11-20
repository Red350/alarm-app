package red.padraig.alarmapp.Extensions

import java.text.SimpleDateFormat
import java.util.*

val SECOND_MILLIS = 1000
val MINUTE_MILLIS = SECOND_MILLIS * 60
val HOUR_MILLIS =  MINUTE_MILLIS * 60

fun Long.getHours() = this / HOUR_MILLIS

fun Long.getMinutes() = this % HOUR_MILLIS / MINUTE_MILLIS

fun Long.toAlarmString(): String {
    if (this < 0) throw RuntimeException("Can't display a negative time!")
    val builder = StringBuilder()
    builder.append(String.format("%02d", this.getHours()))
    builder.append(":")
    builder.append(String.format("%02d", this.getMinutes()))
    return builder.toString()
}

fun Long.fromEpochToDateTimeString(): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm E dd/MM/yyyy")
    return simpleDateFormat.format(Date(this))
}

fun Long.fromEpochToTimeString(): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm")
    return simpleDateFormat.format(Date(this))
}

fun Long.fromEpochToDateString(): String {
    val simpleDateFormat = SimpleDateFormat("E, dd/MM/yyyy")
    return simpleDateFormat.format(Date(this))
}