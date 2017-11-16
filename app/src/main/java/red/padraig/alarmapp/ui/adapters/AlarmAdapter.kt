package red.padraig.alarmapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.alarmrow.view.*
import red.padraig.alarmapp.Alarm
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

        // Set the data for the row
        val alarm = getItem(position) as Alarm
        row?.text_alarmrow_id?.text = alarm.id.toString()
        row?.text_alarmrow_time?.text = alarm.time.toString()
        row?.text_alarmrow_days?.text = alarm.days.toString()
        row?.switch_alarmrow_enabled?.isChecked = alarm.active

        // Set the listener for the enable/disable switch
        row?.switch_alarmrow_enabled?.setOnCheckedChangeListener { _, checked ->
            // TODO: Change this to a callback instead of passing the AlarmDAO instance
            // TODO: (I think this is fixed now) This isn't updating the database, as the _id field isn't matching rowID
            alarmDAO.updateAlarmState(alarm.id, checked)
            Toast.makeText(ctx, "Alarm " + alarm.id + " " + if(checked) "enabled" else "disabled",
                    Toast.LENGTH_SHORT).show()
        }

        // TODO
        // Set the listener for the delete button
        row?.button_alarmrow_delete?.setOnClickListener {
            alarmDAO.deleteAlarm(alarm.id)
            Toast.makeText(ctx, "Alarm " + alarm.id + " deleted",
                    Toast.LENGTH_SHORT).show()
        }

        return row
    }

    fun updateView(newAlarms: List<Alarm>) {
        alarms.clear()
        alarms.addAll(newAlarms)
        notifyDataSetChanged()
    }
}