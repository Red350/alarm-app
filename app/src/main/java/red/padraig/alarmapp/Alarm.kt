package red.padraig.alarmapp


data class Alarm @JvmOverloads constructor(val time: Int, val days: Int, val active: Boolean, val id: Int = -1)