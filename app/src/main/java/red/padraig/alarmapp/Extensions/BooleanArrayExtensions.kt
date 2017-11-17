package red.padraig.alarmapp.Extensions

fun BooleanArray.fromDaysArraytoBinary(): Int {
    var total = 0
    for (b in this) {
        total = total or if (b) 1 else 0
        total = total shl 1
    }
    return total shr 1
}