package com.example.how_not_to_get_divorced.ui.new_alarm

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.how_not_to_get_divorced.ui.new_alarm.sliders.ContinuousNewAlarmSlider
import com.example.how_not_to_get_divorced.ui.new_alarm.sliders.DiscreteNewAlarmSlider
import com.example.how_not_to_get_divorced.ui.new_alarm.sliders.NewAlarmSlider

class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    private var fragments: Array<NewAlarmSlider> = arrayOf(
        DiscreteNewAlarmSlider(true) {v -> unify(v)},
        ContinuousNewAlarmSlider(true) {v -> unify(v)}
    )

    /**
     * Sets collapse on every fragment
     */
    private fun unify(v: Boolean){
        for (frag in fragments){
            frag.setCollapse(v)
        }
    }

    /**
     * Return number of pages
     */
    override fun getItemCount(): Int = 2

    /**
     * Returns fragment with specific index
     */
    override fun createFragment(pos: Int): Fragment = fragments[pos]

    /**
     * Returns NewAlarmSlider with specific index
     */
    fun getAlarmSlider(pos: Int) : NewAlarmSlider = fragments[pos]
}