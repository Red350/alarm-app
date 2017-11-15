package red.padraig.alarmapp.database.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import red.padraig.alarmapp.Alarm
import red.padraig.alarmapp.database.*

class AlarmDAO(context: Context) {

    private val TAG = "AlarmDAO"

    private val databaseHelper = DatabaseHelper(context)
    private val db = databaseHelper.writableDatabase

    fun close() {
        databaseHelper.close()
    }

    fun insertAlarm(alarm: Alarm): Long {
        return db.insert(TABLE_ALARM, null, alarmToCv(alarm))
    }

    fun getAlarms(): List<Alarm> {
        val alarmList = ArrayList<Alarm>()
        val cursor = db.query(true, TABLE_ALARM, null,
                null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            alarmList.add(cursorToAlarm(cursor))
        }


        cursor.close()
        return alarmList
    }

    // TODO(pickup): Have to change the database so that the _id field matches rowID
    // probably by removing _id altogether
    fun updateAlarmState(id: Int, active: Boolean) {
        val whereClause = "_id=" + id
        val cv = ContentValues()
        cv.put(ALARM_COLUMN_ACTIVE, active)
        db.update(TABLE_ALARM, cv, whereClause, null)
    }


    private fun alarmToCv(alarm: Alarm): ContentValues {
        val cv = ContentValues()
        if (alarm.id != -1) {
            cv.put(ALARM_COLUMN_ID, alarm.id)
        }
        cv.put(ALARM_COLUMN_TIME, alarm.time)
        cv.put(ALARM_COLUMN_DAYS, alarm.days)
        cv.put(ALARM_COLUMN_ACTIVE, alarm.active)
        return cv
    }

    private fun cursorToAlarm(cursor: Cursor): Alarm {
        val id = cursor.getInt(cursor.getColumnIndex(ALARM_COLUMN_ID))
        val time = cursor.getInt(cursor.getColumnIndex(ALARM_COLUMN_TIME))
        val days = cursor.getInt(cursor.getColumnIndex(ALARM_COLUMN_DAYS))
        val active = cursor.getInt(cursor.getColumnIndex(ALARM_COLUMN_ACTIVE))
        return Alarm(time, days, (active == 1), id)
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