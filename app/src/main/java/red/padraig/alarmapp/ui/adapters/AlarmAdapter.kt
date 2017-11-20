package red.padraig.alarmapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.alarmrow.view.*
import red.padraig.alarmapp.Extensions.toAlarmString
import red.padraig.alarmapp.alarm.Alarm
import red.padraig.alarmapp.database.dao.AlarmDAO


class AlarmAdapter(val alarmDAO: AlarmDAO, val ctx: Context, val rowLayoutId: Int, val alarms: MutableList<Alarm>) :
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

        /* Set the row data */

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

        /* Set the listeners */

        // Set the listener for the enable/disable switch
        // TODO: Change this to a callback instead of passing the AlarmDAO instance
        row?.switch_alarmrow_enabled?.setOnCheckedChangeListener { _, checked ->
            alarmDAO.updateAlarmState(alarm.id, checked)
        }

        // Set the listener for the delete button
        row?.button_alarmrow_delete?.setOnClickListener { alarmDAO.deleteAlarm(alarm.id) }

        return row
    }

    fun updateView(newAlarms: List<Alarm>) {
        alarms.clear()
        alarms.addAll(newAlarms)
        notifyDataSetChanged()
    }
}