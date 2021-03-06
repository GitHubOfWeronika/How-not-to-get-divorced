package com.example.how_not_to_get_divorced.ui.alarms

import android.content.pm.ActivityInfo
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
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Completion
import java.util.*


class AlarmAdapter(fragment: AlarmsFragment) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private var alarmsList : MutableList<AlarmRecyclerModel> = ArrayList()
    var fragment : AlarmsFragment = fragment
    var orientation = 0 // 0 for portrait, 1 for landscape
    var nav: NavController = fragment.findNavController()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var alarmName : TextView
        var switchAlarm : Switch
        var imageAlarmCategory : ImageView
        var imageViewDayArray :ArrayList<ImageView> = ArrayList()
        var rootContainer : CardView
        var relativeContainer : RelativeLayout
        //TODO(other stuff)

        init{
            rootContainer = view.findViewById(R.id.rootContainer)
            relativeContainer = view.findViewById(R.id.relativeContainer)
            alarmName = view.findViewById(R.id.taskName)
            switchAlarm = view.findViewById(R.id.switchAlarm)
            imageAlarmCategory = view.findViewById(R.id.imageViewTaskCategory)
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay1))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay2))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay3))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay4))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay5))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay6))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay7))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay8))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay9))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay10))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay11))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay12))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay13))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay14))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay15))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay16))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay17))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay18))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay19))
            imageViewDayArray.add(view.findViewById(R.id.imageViewDay20))
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

    fun getAlarmId(position : Int) : Int{
        return alarmsList[position].alarm.id!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item : AlarmRecyclerModel = alarmsList[position]
        holder.alarmName.text=item.alarm.name

        if(fragment.activity?.resources?.configuration?.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            orientation = 0
            portraitImageViews(holder)
        }
        else{
            orientation = 1
            landscapeImageViews(holder)
        }

        //TODO: Make this better


        //TODO: Fragment to access edit alarm
        /*holder.alarmName.setOnClickListener {
            val bundle = bundleOf("alarmId" to item.alarm.id)
            nav.navigate(R.id.getStat, bundle)
        }
        holder.alarmName.setOnClickListener {
            val bundle = bundleOf("alarmId" to item.alarm.id)
            nav.navigate(R.id.action_nav_alarms_to_editAlarmFragment, bundle)
        }*/


        setStatistics(holder,item.alarm.repetition,item.statistics)

        holder.switchAlarm.isChecked = item.alarm.active

        holder.switchAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
                Thread {
                    var alarm = item.alarm
                    alarm.active=isChecked
                    DBAccess(this.fragment.requireContext()).updateAlarm(alarm)
                }.start()
        }
    }

    private fun setStatistics(holder: ViewHolder,repetition: AlarmRepetition, statistics : Array<Map<Completion, Int>>){
        if(repetition.getType()=="Discrete"){
            if(orientation==1) {
                for (i in 0 until holder.imageViewDayArray.size) {
                    setDiscreteStatisticImage(holder.imageViewDayArray[i], statistics[20 - i - 1][Completion.DONE], statistics[20 - i - 1][Completion.FAILED], statistics[20 - i - 1][Completion.WAITING])
                }
            }
            else{
                for (i in 0 until 10) {
                    setDiscreteStatisticImage(holder.imageViewDayArray[i], statistics[10 - i - 1][Completion.DONE], statistics[10 - i - 1][Completion.FAILED], statistics[10 - i - 1][Completion.WAITING])
                }
            }
        }
        if (repetition.getType()=="Continuous"){
            if(orientation==1) {
                for (i in 0 until holder.imageViewDayArray.size) {
                    setContinuousStatisticImage(holder.imageViewDayArray[i], statistics[20 - i - 1][Completion.DONE], statistics[20 - i - 1][Completion.FAILED], statistics[20 - i - 1][Completion.WAITING])
                }
            }
            else{
                for(i in 0 until 10){
                    setContinuousStatisticImage(holder.imageViewDayArray[i], statistics[10 - i - 1][Completion.DONE], statistics[10 - i - 1][Completion.FAILED], statistics[10 - i - 1][Completion.WAITING])
                }
            }
        }

    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    private fun portraitImageViews(holder: ViewHolder){
        for (i in 10 until holder.imageViewDayArray.size){
            holder.imageViewDayArray[i].visibility=View.GONE
        }
    }

    private fun landscapeImageViews(holder: ViewHolder){
        for (i in 10 until holder.imageViewDayArray.size){
            holder.imageViewDayArray[i].visibility=View.VISIBLE
        }
    }

    fun setTasks(alarmRecyclerList : MutableList<AlarmRecyclerModel>){
    this.alarmsList = alarmRecyclerList
    notifyDataSetChanged()
    }

    fun setDiscreteStatisticImage(imageView: ImageView, done: Int?, canceled: Int?, waiting: Int?){
        if(done!! +canceled!!+waiting!!>=1)  imageView.setImageResource(R.drawable.square_icon2)
        else imageView.setImageResource(R.drawable.square_icon5)
    }

    fun setContinuousStatisticImage(imageView: ImageView, done: Int?, canceled: Int?, waiting: Int?){
        Log.d("recykler_test", waiting!!.toString());
        if(done!! +canceled!!+waiting!!>=32)  imageView.setImageResource(R.drawable.circle_icon1)
        else if(done!! +canceled!!+waiting!!>=16)  imageView.setImageResource(R.drawable.circle_icon2)
        else if(done!! +canceled!!+waiting!!>=8)  imageView.setImageResource(R.drawable.circle_icon3)
        else if(done!! +canceled!!+waiting!!>=2)  imageView.setImageResource(R.drawable.circle_icon4)
        else imageView.setImageResource(R.drawable.circle_icon5)
    }
}