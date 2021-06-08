package com.example.how_not_to_get_divorced.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Completion
import com.example.how_not_to_get_divorced.model.Task
import com.example.how_not_to_get_divorced.ui.alarms.AlarmAdapter
import com.example.how_not_to_get_divorced.ui.alarms.AlarmRecyclerModel
import com.example.how_not_to_get_divorced.ui.alarms.AlarmsModel
import com.example.how_not_to_get_divorced.ui.utils.SwipeToEdit
import com.example.how_not_to_get_divorced.ui.utils.SwipeToStats
import java.util.*

class TaskFragment : Fragment() {

    lateinit var tasksRecyclerView: RecyclerView
    lateinit var taskAdapter : TaskAdapter
    var taskModelsList : MutableList<Task> = ArrayList<Task>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_task, container, false)

        tasksRecyclerView = root.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this.context)
        taskAdapter= TaskAdapter(this)
        taskAdapter.fragment=this
        tasksRecyclerView.adapter=taskAdapter



        val db = DBAccess(requireContext())
        db.getAllTaskForADay(getDaysAgo(1)).observe(viewLifecycleOwner, androidx.lifecycle.Observer { it : List<Task> ->
            taskModelsList.clear() // Czyślimy obecną listę żeby wypełnić ją na nowo
            taskAdapter.setTasks(taskModelsList) // podpinamy listę do recykler view
            it.forEach {task -> //dla każdego alarmu
                taskModelsList.add(task) //Dodajemy go na liste
            }
        })


        return root
    }
    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }
}