package red.padraig.alarmapp.util

import java.util.*

fun getTodaysIndex(): Int {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_WEEK)

    // The calendar class starts on Sunday, my days start on Monday
    // Also the day's enum start at 1, hence the need to subtract 2 instead of 1
    if (day == Calendar.SUNDAY) {
        return 6
    } else {
        return day - 2
    }
}