package red.padraig.alarmapp

// TODO: maybe store days as a boolean array here, and convert to/from int at database level
data class Alarm (var id: Long, val time: Long, val days: Int, val active: Boolean)