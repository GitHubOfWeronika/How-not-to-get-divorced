package com.example.how_not_to_get_divorced.ui.new_alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.Category
import com.example.how_not_to_get_divorced.ui.customSpinner.CustomAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText

/**
 * Fragment of "new alarm" page
 */
class NewAlarmFragment : Fragment() {
    private lateinit var root: View
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_new_alarm, container, false)
        //Prepare category spinner
        val categorySpinner : Spinner = root.findViewById(R.id.new_alarm_category)
        categorySpinner.adapter = CustomAdapter(requireContext(), Category.CATEGORIES_LIST.toList())

        //Prepare view pager for selecting alarm type
        viewPager = root.findViewById(R.id.slider_new_alarm_pager)
        pagerAdapter = ScreenSlidePagerAdapter(this)
        val tabLayout = root.findViewById<TabLayout>(R.id.slider_new_alarm_tabs)
        viewPager.adapter = pagerAdapter

        //Connecting view pager with tab bar
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.discrete)
                    //tab.icon = ResourcesCompat.getDrawable(requireContext().resources, R.drawable.settings_icon, requireContext().theme)
                }
                1 -> {
                    tab.text = getString(R.string.continuous)
                }
            }
        }.attach()

        // Create button handling
        root.findViewById<Button>(R.id.new_alarm_create).setOnClickListener {
            createAlarm()
        }

        return root
    }

    /**
     * Tries to create alarm and navigate to alarm page
     */
    private fun createAlarm(){
        val navController = findNavController()
        val alarm = getAlarm()
        if (alarm != null){
            Thread {
                DBAccess(requireContext()).insertAlarm(alarm)
            }.start()
            navController.navigate(R.id.nav_alarms)
        }
    }

    /**
     * Generates alarm from current user input or show a toast
     */
    private fun getAlarm() : Alarm?{
        val name = root.findViewById<TextInputEditText>(R.id.new_alarm_name).text.toString().trim()
        val category = Category.CATEGORIES_LIST[root.findViewById<Spinner>(R.id.new_alarm_category).selectedItemPosition]
        val repetition = pagerAdapter.getAlarmSlider(viewPager.currentItem).getResult()
        if (name == "") {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return null
        } else if (repetition.getExpectedNumberOfTriggers() <= 0.001) {
            Toast.makeText(requireContext(), "It will never trigger", Toast.LENGTH_SHORT).show()
            return null
        } else {
            return Alarm(null, name, category, repetition)
        }
    }
}