package red.padraig.alarmapp.Extensions

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
