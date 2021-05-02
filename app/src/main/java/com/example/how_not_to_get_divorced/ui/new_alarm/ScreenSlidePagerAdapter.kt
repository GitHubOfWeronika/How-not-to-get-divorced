package com.example.how_not_to_get_divorced.ui.new_alarm

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.how_not_to_get_divorced.ui.new_alarm.sliders.ContinuousNewAlarmSlider
import com.example.how_not_to_get_divorced.ui.new_alarm.sliders.DiscreteNewAlarmSlider
import com.example.how_not_to_get_divorced.ui.new_alarm.sliders.NewAlarmSlider

class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

    private val id = 0L
    private var fragments: Array<NewAlarmSlider> = arrayOf(
        DiscreteNewAlarmSlider(),
        ContinuousNewAlarmSlider()
    )

    init {
        fragments[0].changeCollapse = {unify(it)}
        fragments[1].changeCollapse = {unify(it)}
    }

    /**
     * Sets collapse on every fragment
     */
    private fun unify(v: Boolean){
        for (frag in fragments){
            frag.collapse = v
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

    override fun getItemId(position: Int) = id + position

    override fun containsItem(itemId: Long) = itemId >= id && itemId < id + fragments.size
}