package red.padraig.alarmapp.Extensions

// Int extensions
fun Int.fromHourToMills() = this * 60 * 60 * 1000

fun Int.fromMinutesToMillis() = this * 60

// Boolean array extensions
fun BooleanArray.toBinary(): Int {
    var total = 0
    for (b in this) {
        total = total or if (b) 1 else 0
        total = total shl 1
    }
    return total shr 1
}