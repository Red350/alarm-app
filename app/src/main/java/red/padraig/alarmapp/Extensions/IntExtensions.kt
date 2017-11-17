package red.padraig.alarmapp.Extensions

fun Int.fromMinutesToMillis() = this * MINUTE_MILLIS

fun Int.fromHoursToMills() = this * HOUR_MILLIS

fun Int.fromBinaryToDaysArray(): BooleanArray {
    if ((this > 127) or (this < 0)) throw RuntimeException("Number is too big to be converted to days array!")
    val arr = BooleanArray(7)
    for (i in 0..6) {
        arr[i] = this and (1 shl (6 - i)) != 0    // i should be 6 - i
    }
    return arr
}