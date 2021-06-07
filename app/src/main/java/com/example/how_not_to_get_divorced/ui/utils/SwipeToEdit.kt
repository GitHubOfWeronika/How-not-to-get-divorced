package com.example.how_not_to_get_divorced.ui.utils

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.ui.alarms.AlarmAdapter

class SwipeToEdit (var adapter: AlarmAdapter): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position: Int = viewHolder.adapterPosition
        var itemId = adapter.getAlarmId(position)
        val bundle = bundleOf("alarmId" to itemId)
        adapter.nav.navigate(R.id.action_nav_alarms_to_editAlarmFragment, bundle)
    }
}

