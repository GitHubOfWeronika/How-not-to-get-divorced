package com.example.how_not_to_get_divorced.ui.alarms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Category
import java.sql.Types.NULL
import java.util.*
import kotlin.collections.ArrayList

class AlarmsFragment : Fragment() {

    private lateinit var alarmsModel: AlarmsModel
    lateinit var alarmsRecyclerView: RecyclerView
    lateinit var alarmAdapter :AlarmAdapter
    var alarmModelsList : MutableList<AlarmRecyclerModel> = ArrayList<AlarmRecyclerModel>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //alarmsModel =
         //       ViewModelProvider(this).get(AlarmsModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarms, container, false)

        alarmsRecyclerView = root.findViewById(R.id.tasksRecyclerView)
        alarmsRecyclerView.layoutManager = LinearLayoutManager(root.context)
        alarmAdapter= AlarmAdapter(this)
        alarmAdapter.fragment=this
        alarmsRecyclerView.adapter=alarmAdapter

        val db = DBAccess(requireContext())
        db.getAllAlarms().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var alarms = db.getAllAlarms()
            alarms.value?.forEach {
                alarmModelsList.add(AlarmRecyclerModel(it,db.getStatistics(it,getDaysAgo(10))))
            }
            alarmAdapter.setTasks(alarmModelsList)
            alarmAdapter.notifyDataSetChanged()
        })
        /*var alarms = db.getAllAlarms()
        alarms.value?.forEach {
            alarmModelsList.add(AlarmRecyclerModel(it,db.getStatistics(it,getDaysAgo(10))))
        }

        alarmAdapter.setTasks(alarmModelsList)
        alarmAdapter.notifyDataSetChanged()*/

        return root
    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }
}