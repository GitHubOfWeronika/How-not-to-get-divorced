package com.example.how_not_to_get_divorced.ui.alarms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Completion
import java.util.*
import kotlin.collections.ArrayList

class AlarmAdapter(fragment: AlarmsFragment) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private var alarmsList : MutableList<AlarmRecyclerModel> = ArrayList()
    var fragment : AlarmsFragment = fragment

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var alarmName : TextView
        var switchAlarm : Switch
        var imageAlarmCategory : ImageView
        var imageViewDay1 : ImageView
        var imageViewDay2 : ImageView
        var imageViewDay3 : ImageView
        var imageViewDay4 : ImageView
        var imageViewDay5 : ImageView
        var imageViewDay6 : ImageView
        var imageViewDay7 : ImageView
        var imageViewDay8 : ImageView
        var imageViewDay9 : ImageView
        var imageViewDay10 : ImageView
        //TODO(other stuff)

        init{
            alarmName = view.findViewById(R.id.alarmName)
            switchAlarm = view.findViewById(R.id.switchAlarm)
            imageAlarmCategory = view.findViewById(R.id.imageViewAlarmCategory)
            imageViewDay1 = view.findViewById(R.id.imageViewDay1)
            imageViewDay2 = view.findViewById(R.id.imageViewDay2)
            imageViewDay3 = view.findViewById(R.id.imageViewDay3)
            imageViewDay4 = view.findViewById(R.id.imageViewDay4)
            imageViewDay5 = view.findViewById(R.id.imageViewDay5)
            imageViewDay6 = view.findViewById(R.id.imageViewDay6)
            imageViewDay7 = view.findViewById(R.id.imageViewDay7)
            imageViewDay8 = view.findViewById(R.id.imageViewDay8)
            imageViewDay9 = view.findViewById(R.id.imageViewDay9)
            imageViewDay10 = view.findViewById(R.id.imageViewDay10)
            //TODO(other stuff)

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
        holder.alarmName.text=item.alarm.name
        setStatistics(holder,item.alarm.repetition,item.statistics)
        TODO("Not yet implemented")
    }

    private fun setStatistics(holder: ViewHolder,repetition: AlarmRepetition, statistics : Array<Map<Completion, Int>>){
        if(repetition.getType()=="Discrete"){
            setDiscreteStatisticImage(holder.imageViewDay1, statistics[9][Completion.DONE],statistics.get(9)?.get(Completion.FAILED),statistics.get(9).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay2, statistics.get(8).get(Completion.DONE),statistics.get(8).get(Completion.FAILED),statistics.get(8).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay3, statistics.get(7).get(Completion.DONE),statistics.get(7).get(Completion.FAILED),statistics.get(7).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay4, statistics.get(6).get(Completion.DONE),statistics.get(6).get(Completion.FAILED),statistics.get(6).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay5, statistics.get(5).get(Completion.DONE),statistics.get(5).get(Completion.FAILED),statistics.get(5).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay6, statistics.get(4).get(Completion.DONE),statistics.get(4).get(Completion.FAILED),statistics.get(4).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay7, statistics.get(3).get(Completion.DONE),statistics.get(3).get(Completion.FAILED),statistics.get(3).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay8, statistics.get(2).get(Completion.DONE),statistics.get(2).get(Completion.FAILED),statistics.get(2).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay9, statistics.get(1).get(Completion.DONE),statistics.get(1).get(Completion.FAILED),statistics.get(1).get(Completion.WAITING))
            setDiscreteStatisticImage(holder.imageViewDay10, statistics.get(0).get(Completion.DONE),statistics.get(0).get(Completion.FAILED),statistics.get(0).get(Completion.WAITING))
        }
        if (repetition.getType()=="Continuous"){
            setContinuousStatisticImage(holder.imageViewDay1, statistics[9][Completion.DONE],statistics.get(9)?.get(Completion.FAILED),statistics.get(9).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay2, statistics.get(8).get(Completion.DONE),statistics.get(8).get(Completion.FAILED),statistics.get(8).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay3, statistics.get(7).get(Completion.DONE),statistics.get(7).get(Completion.FAILED),statistics.get(7).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay4, statistics.get(6).get(Completion.DONE),statistics.get(6).get(Completion.FAILED),statistics.get(6).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay5, statistics.get(5).get(Completion.DONE),statistics.get(5).get(Completion.FAILED),statistics.get(5).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay6, statistics.get(4).get(Completion.DONE),statistics.get(4).get(Completion.FAILED),statistics.get(4).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay7, statistics.get(3).get(Completion.DONE),statistics.get(3).get(Completion.FAILED),statistics.get(3).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay8, statistics.get(2).get(Completion.DONE),statistics.get(2).get(Completion.FAILED),statistics.get(2).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay9, statistics.get(1).get(Completion.DONE),statistics.get(1).get(Completion.FAILED),statistics.get(1).get(Completion.WAITING))
            setContinuousStatisticImage(holder.imageViewDay10, statistics.get(0).get(Completion.DONE),statistics.get(0).get(Completion.FAILED),statistics.get(0).get(Completion.WAITING))
        }

    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    fun setTasks(alarmRecyclerList : MutableList<AlarmRecyclerModel>){
        this.alarmsList = alarmRecyclerList
        notifyDataSetChanged()
    }

    fun setDiscreteStatisticImage(imageView: ImageView, done: Int?, canceled: Int?, waiting: Int?){
        if(done!! +canceled!!+waiting!!>=1)  imageView.setImageResource(R.drawable.square_icon1)
        else imageView.setImageResource(R.drawable.square_icon5)
    }

    fun setContinuousStatisticImage(imageView: ImageView, done: Int?, canceled: Int?, waiting: Int?){
        if(done!! +canceled!!+waiting!!>=32)  imageView.setImageResource(R.drawable.circle_icon1)
        else if(done!! +canceled!!+waiting!!>=16)  imageView.setImageResource(R.drawable.circle_icon2)
        else if(done!! +canceled!!+waiting!!>=8)  imageView.setImageResource(R.drawable.circle_icon3)
        else if(done!! +canceled!!+waiting!!>=2)  imageView.setImageResource(R.drawable.circle_icon4)
        else imageView.setImageResource(R.drawable.circle_icon5)
    }
}