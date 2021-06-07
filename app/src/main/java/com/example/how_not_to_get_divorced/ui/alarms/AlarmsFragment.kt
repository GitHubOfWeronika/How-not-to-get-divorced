package com.example.how_not_to_get_divorced.ui.alarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Completion
import com.example.how_not_to_get_divorced.ui.utils.SwipeToEdit
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
        alarmsModel =
               ViewModelProvider(this).get(AlarmsModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarms, container, false)

        alarmsRecyclerView = root.findViewById(R.id.alarmsRecyclerView)
        alarmsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        alarmAdapter= AlarmAdapter(this)
        alarmAdapter.fragment=this
        alarmsRecyclerView.adapter=alarmAdapter

        var swipeToDeleteHelper = ItemTouchHelper(SwipeToEdit(alarmAdapter))
        swipeToDeleteHelper.attachToRecyclerView(alarmsRecyclerView)

        val db = DBAccess(requireContext())
        db.getAllAlarms().observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            alarmModelsList.clear() // Czyślimy obecną listę żeby wypełnić ją na nowo
            alarmAdapter.setTasks(alarmModelsList) // podpinamy listę do recykler view
            it.forEach {alarm -> //dla każdego alarmu
                val alarmModel = AlarmRecyclerModel(alarm, Array(20) { mutableMapOf(
                        Completion.DONE to 0,
                        Completion.FAILED to 0,
                        Completion.WAITING to 0)
                }) //Tworzymy tutaj model dla naszego alarmu, w placeholderem dla ststystyk (normalnie dałbym tu emptyArray())
                alarmModelsList.add(alarmModel) //Dodajemy go na liste
                (db.getStatistics(alarm,getDaysAgo(20))).observe(viewLifecycleOwner,androidx.lifecycle.Observer { stat -> // Obserwujemy teraz sttystyki
                    alarmModel.statistics = stat // podpinamy nowe ststyskyki dla naszego modelu
                    alarmAdapter.notifyDataSetChanged() // informujemy recykler view że coś się zmieniło
                })
            }
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