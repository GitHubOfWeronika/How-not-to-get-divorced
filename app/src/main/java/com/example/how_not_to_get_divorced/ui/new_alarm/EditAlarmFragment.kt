package com.example.how_not_to_get_divorced.ui.new_alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Category
import com.example.how_not_to_get_divorced.model.Category.Companion.CATEGORIES_LIST
import com.example.how_not_to_get_divorced.ui.customSpinner.CustomAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText

class EditAlarmFragment : Fragment() {
    private lateinit var root: View
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private lateinit var viewPager: ViewPager2
    private var alarmId: Int = 0
    private lateinit var db : DBAccess
    private lateinit var alarm : Alarm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_edit_alarm, container, false)
        db = DBAccess(requireContext())

        //Prepare category spinner
        val categorySpinner : Spinner = root.findViewById(R.id.edit_alarm_category)
        categorySpinner.adapter = CustomAdapter(requireContext(), Category.CATEGORIES_LIST.toList())

        //Prepare view pager for selecting alarm type
        viewPager = root.findViewById(R.id.slider_edit_alarm_pager)
        pagerAdapter = ScreenSlidePagerAdapter(this)
        val tabLayout = root.findViewById<TabLayout>(R.id.slider_edit_alarm_tabs)
        viewPager.adapter = pagerAdapter
        pagerAdapter.notifyDataSetChanged()

        //Connecting view pager with tab bar
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.discrete)
                }
                1 -> {
                    tab.text = getString(R.string.continuous)
                }
            }
        }.attach()

        val arguments = arguments
        if (arguments != null){
            alarmId = arguments.getInt("alarmId")
            db.getAlarmById(alarmId).observe(viewLifecycleOwner, androidx.lifecycle.Observer { alarm ->
                this.alarm = alarm
                root.findViewById<TextInputEditText>(R.id.edit_alarm_name).setText(alarm.name)
                root.findViewById<Spinner>(R.id.edit_alarm_category).setSelection(CATEGORIES_LIST.indexOf(alarm.category))
                pagerAdapter.setResult(alarm.repetition)
                Thread {
                    Thread.sleep(50)
                    when (alarm.repetition) {
                        is AlarmRepetition.Continuous -> viewPager.currentItem = 1
                        is AlarmRepetition.Discrete -> viewPager.currentItem = 0
                    }
                }.start()
            })
        }

        root.findViewById<Button>(R.id.edit_alarm).setOnClickListener { editAlarm() }
        root.findViewById<Button>(R.id.delete_edit_alarm).setOnClickListener { deleteAlarm() }
        root.findViewById<Button>(R.id.cancel_edit_alarm).setOnClickListener { cancel() }

        return root
    }

    /**
     * Tries to edit
     */
    private fun editAlarm(){
        val editedAlarm = getAlarm()
        if (editedAlarm != null){
            Thread {
               db.updateAlarm(editedAlarm)
            }.start()
            findNavController().navigate(R.id.action_return_from_alarm)
        }
    }

    private fun deleteAlarm(){
        Thread {
            alarm.deleted = true
            alarm.active = false
            db.updateAlarm(alarm)
        }.start()
        findNavController().navigate(R.id.action_return_from_alarm)
    }

    private fun cancel(){
        findNavController().navigate(R.id.action_return_from_alarm)
    }

    /**
     * Generates alarm from current user input or show a toast
     */
    private fun getAlarm() : Alarm?{
        val name = root.findViewById<TextInputEditText>(R.id.edit_alarm_name).text.toString().trim()
        val category = Category.CATEGORIES_LIST[root.findViewById<Spinner>(R.id.edit_alarm_category).selectedItemPosition]
        val repetition = pagerAdapter.getAlarmSlider(viewPager.currentItem).getResult()
        if (name == "") {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return null
        } else if (repetition.getExpectedNumberOfTriggers() <= 0.001) {
            Toast.makeText(requireContext(), "It will never trigger", Toast.LENGTH_SHORT).show()
            return null
        } else {
            return Alarm(alarmId, name, category, repetition, alarm.active, alarm.deleted, alarm.created)
        }
    }
}