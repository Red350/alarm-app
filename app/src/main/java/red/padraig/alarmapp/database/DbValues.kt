package red.padraig.alarmapp.database

val DB_NAME    = "alarmdb"
val DB_VERSION = 1

val TYPE_INT  = "INTEGER"
val TYPE_TEXT = "TEXT"

val TABLE_ALARM = "alarm"
val ALARM_COLUMN_ID     = "_id"
val ALARM_COLUMN_TIME   = "time"
val ALARM_COLUMN_DAYS   = "days"
val ALARM_COLUMN_ACTIVE = "active"

val CREATE_ALARM_TABLE =
        "CREATE TABLE " + TABLE_ALARM + "(" +
                ALARM_COLUMN_ID + " " + TYPE_INT + " PRIMARY KEY AUTOINCREMENT, " +
                ALARM_COLUMN_TIME + " " + TYPE_INT + ", " +
                ALARM_COLUMN_DAYS + " " + TYPE_INT + ", " +
                ALARM_COLUMN_ACTIVE + " " + TYPE_INT + ");"
