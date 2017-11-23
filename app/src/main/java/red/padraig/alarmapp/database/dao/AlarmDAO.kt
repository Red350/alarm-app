package red.padraig.alarmapp.database.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import io.reactivex.processors.PublishProcessor
import red.padraig.alarmapp.Extensions.fromBinaryToDaysArray
import red.padraig.alarmapp.Extensions.fromDaysArraytoBinary
import red.padraig.alarmapp.alarm.Alarm
import red.padraig.alarmapp.database.*

class AlarmDAO(context: Context) {

    private val TAG = "AlarmDAO"

    private val databaseHelper = DatabaseHelper(context)
    private val db = databaseHelper.writableDatabase

    // Stream of alarms that are updated
    // UI elements can subscribe to this to ensure they are displaying the most up-to-date data
    // TODO: add a separate stream for new alarms instead of using updatedAlarms
    val updatedAlarms: PublishProcessor<Alarm> = PublishProcessor.create()
    val deletedAlarmIds: PublishProcessor<Long> = PublishProcessor.create()

    fun close() {
        databaseHelper.close()
    }

    fun createAlarm(time: Long, days: BooleanArray, active: Boolean): Long {
        val cv = loadContentValues(time, days.fromDaysArraytoBinary(), active)

        val id = db.insert(TABLE_ALARM, null, cv)

        // Failed to insert an alarm
        if (id == -1L) throw RuntimeException("Error inserting alarm")

        Log.d(TAG, "Inserted alarm with id: $id")
        // Emit the newly created alarm
        updatedAlarms.onNext(Alarm(id, time, days, active))
        return id
    }

    fun updateAlarm(id: Long, time: Long, days: BooleanArray, active: Boolean): Int {
        val cv = loadContentValues(time, days.fromDaysArraytoBinary(), active)

        val result = db.update(TABLE_ALARM, cv, "_id=$id", null)

        // Failed to update alarm
        if (result != 1) throw RuntimeException("Error updating alarm, updated $result alarm(s) (id: $id)")

        Log.d(TAG, "Updated alarm with id: $id")
        // Emit the updated alarm
        updatedAlarms.onNext(Alarm(id, time, days, active))
        return result
    }

    fun deleteAlarm(id: Long): Int {
        val result = db.delete(TABLE_ALARM, "_id = " + id, null)

        // Failed to delete an alarm
        if (result != 1) throw RuntimeException("Error deleting alarm, deleted $result alarm(s) (id: $id)")

        Log.d(TAG, "Deleted alarm with id: $id")
        // Emit the id of the deleted alarm
        deletedAlarmIds.onNext(id)
        return result
    }

    fun getAlarmById(id: Long): Alarm? {
        val cursor = db.query(true, TABLE_ALARM, null,
                "_id=$id", null, null, null, null, null)
        var alarm: Alarm? = null
        if (cursor.moveToFirst())
            alarm = cursorToAlarm(cursor)

        cursor.close()
        return alarm
    }

    fun getAlarms(): MutableList<Alarm> {
        val alarmList = ArrayList<Alarm>()
        val cursor = db.query(true, TABLE_ALARM, null,
                null, null, null, null, ALARM_COLUMN_TIME, null)

        while (cursor.moveToNext()) {
            alarmList.add(cursorToAlarm(cursor))
        }

        cursor.close()
        return alarmList
    }

    // Updates whether the alarm is currently enabled or disabled
    fun updateAlarmState(id: Long, active: Boolean): Int {
        val cv = ContentValues()
        cv.put(ALARM_COLUMN_ACTIVE, active)
        val result = db.update(TABLE_ALARM, cv, "_id=" + id, null)

        if (result == -1) throw RuntimeException("Error updating alarm state, id: $id")

        Log.d(TAG, if (active) "Enabled" else "Disabled" + " alarm with id: $id")
        // Emit the id of the updated alarm
        // Must check if the alarm is null, as it's possible the alarm is deleted before the alarm is emitted
        val alarm: Alarm? = getAlarmById(id)
        if (alarm != null) updatedAlarms.onNext(alarm)
        return result
    }

    private fun cursorToAlarm(cursor: Cursor): Alarm {
        val id = cursor.getLong(cursor.getColumnIndex(ALARM_COLUMN_ID))
        val time = cursor.getLong(cursor.getColumnIndex(ALARM_COLUMN_TIME))
        val days = cursor.getInt(cursor.getColumnIndex(ALARM_COLUMN_DAYS))
        val active = cursor.getInt(cursor.getColumnIndex(ALARM_COLUMN_ACTIVE))
        return Alarm(id, time, days.fromBinaryToDaysArray(), (active == 1))
    }

    private fun loadContentValues(time: Long, days: Int, active: Boolean): ContentValues {
        val cv = ContentValues()
        cv.put(ALARM_COLUMN_TIME, time)
        cv.put(ALARM_COLUMN_DAYS, days)
        cv.put(ALARM_COLUMN_ACTIVE, active)
        return cv
    }

    inner class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

        override fun onCreate(db: SQLiteDatabase?) {
            Log.d(TAG, CREATE_ALARM_TABLE)
            db?.execSQL(CREATE_ALARM_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        }
    }
}