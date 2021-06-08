package com.example.how_not_to_get_divorced.ui.tasks

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Completion
import com.example.how_not_to_get_divorced.model.Task

import java.util.*

class TaskAdapter(fragment: TaskFragment) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private var tasksList : MutableList<Task> = ArrayList()
    var fragment : TaskFragment = fragment
    var orientation = 0 // 0 for portrait, 1 for landscape
    var nav: NavController = fragment.findNavController()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var taskName : TextView
        var taskCancel : ImageButton
        var taskComplete : ImageButton
        var container : RelativeLayout

        //TODO(other stuff)

        init{
            taskName=view.findViewById(R.id.taskName)
            taskCancel = view.findViewById(R.id.imageButtonCancel)
            taskComplete = view.findViewById(R.id.imageButtonComplete)
            container = view.findViewById(R.id.relativeContainer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun getTaskId(position : Int) : Int{
        return tasksList[position].id!!
    }


    fun setTasks(tasksList : MutableList<Task>){
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item : Task = tasksList[position]
        holder.taskName.text=item.alarm.name.toString()+" "+ ((((item.date.time/1000)%(3600*24))/3600)+2).toString() + ":" + ifNeedZeroAddZero(((item.date.time/1000)%(3600))/60)
        if(item.completion==Completion.FAILED){
            holder.container.setBackgroundColor(Color.argb(0.2f,1.0f,0.0f,0.0f))
        }
        if(item.completion==Completion.DONE){
            holder.container.setBackgroundColor(Color.argb(0.2f,0.0f,1.0f,0.0f))
        }

        holder.taskCancel.setOnClickListener(){
            if(item.completion!=Completion.FAILED){
                item.completion=Completion.FAILED
            }
            else if (item.completion==Completion.FAILED){
                item.completion=Completion.WAITING
            }
            item.changed=Calendar.getInstance().time
            Thread {
                DBAccess(this.fragment.requireContext()).updateTest(item)
            }.start()
            notifyDataSetChanged()
        }
        holder.taskComplete.setOnClickListener(){
            if(item.completion!=Completion.DONE){
                item.completion=Completion.DONE
            }
            else if (item.completion==Completion.DONE){
                item.completion=Completion.WAITING
            }
            item.changed=Calendar.getInstance().time
            Thread {
                DBAccess(this.fragment.requireContext()).updateTest(item)
            }.start()
            notifyDataSetChanged()
        }

    }

    fun ifNeedZeroAddZero(minutes: Long):String{
        if(minutes<10){
            return "0$minutes"
        }
        else
            return "$minutes"
    }



}
