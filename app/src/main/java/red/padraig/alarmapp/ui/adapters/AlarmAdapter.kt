package red.padraig.alarmapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.alarmrow.view.*
import red.padraig.alarmapp.Extensions.toAlarmString
import red.padraig.alarmapp.alarm.Alarm
import red.padraig.alarmapp.callbacks.DeleteAlarmCallback
import red.padraig.alarmapp.callbacks.UpdateAlarmStateCallback


class AlarmAdapter(
        val ctx: Context,
        val rowLayoutId: Int,
        val alarms: MutableList<Alarm>,
        private val updateAlarmStateCallback: UpdateAlarmStateCallback,
        private val deleteAlarmCallback: DeleteAlarmCallback) :
        BaseAdapter() {

    override fun getItem(i: Int): Alarm {
        return alarms[i]
    }

    override fun getItemId(i: Int): Long {
        return alarms[i].id
    }

    override fun getCount(): Int {
        return alarms.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val layoutInflater = LayoutInflater.from(ctx)
        var row: View? = convertView

        if (row == null) {
            row = layoutInflater.inflate(rowLayoutId, parent, false)
        }


        /* Set row data */

        val alarm = getItem(position)
        row?.text_alarmrow_time?.text = alarm.time.toAlarmString()

        // Set which days the alarm is set for
        row?.text_alarmrow_monday?.isEnabled = alarm.days[0]
        row?.text_alarmrow_tuesday?.isEnabled = alarm.days[1]
        row?.text_alarmrow_wednesday?.isEnabled = alarm.days[2]
        row?.text_alarmrow_thursday?.isEnabled = alarm.days[3]
        row?.text_alarmrow_friday?.isEnabled = alarm.days[4]
        row?.text_alarmrow_saturday?.isEnabled = alarm.days[5]
        row?.text_alarmrow_sunday?.isEnabled = alarm.days[6]

        // Set whether the alarm is currently enabled
        row?.switch_alarmrow_enabled?.isChecked = alarm.active


        /* Set listeners */

        // Set the listener for the enable/disable switch
        row?.switch_alarmrow_enabled?.setOnCheckedChangeListener { _, checked ->
            updateAlarmStateCallback.updateAlarmState(alarm.id, checked)
        }

        // Set the listener for the delete button
        row?.imagebutton_alarmrow_delete?.setOnClickListener { deleteAlarmCallback.deleteAlarm(alarm.id) }

        return row
    }

    fun updateView(newAlarms: List<Alarm>) {
        alarms.clear()
        alarms.addAll(newAlarms)
        notifyDataSetChanged()
    }

    fun updateRow(alarm: Alarm) {
        (0 until alarms.size)
                .first { alarms[it].id == alarm.id }
                .let {
                    alarms[it] = alarm
                }
    }

    fun deleteRow(index: Int) {
        alarms.removeAt(index)
        notifyDataSetChanged()
    }
}