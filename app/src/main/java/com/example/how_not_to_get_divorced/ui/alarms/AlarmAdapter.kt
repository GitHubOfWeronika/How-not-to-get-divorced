package com.example.how_not_to_get_divorced.ui.alarms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R

class AlarmAdapter(fragment: AlarmsFragment) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private lateinit var alarmsList : List<AlarmRecyclerModel>
    var fragment : AlarmsFragment = fragment

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var task : CheckBox
        //TODO(other stuff)
        var imageType : ImageView
        init{
            task = view.findViewById(R.id.alarmName)
            //TODO(other stuff)
            imageType = view.findViewById(R.id.imageViewType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.alarm_layout, parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return alarmsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item : AlarmRecyclerModel = alarmsList[position]
        TODO("Not yet implemented")
    }
}