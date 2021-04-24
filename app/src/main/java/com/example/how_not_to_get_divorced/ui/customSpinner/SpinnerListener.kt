package com.example.how_not_to_get_divorced.ui.customSpinner

import android.view.View
import android.widget.AdapterView

/**
 * Simple spinner listener
 *
 * f - function to call on change
 */
class SpinnerListener (f: (Int) -> Unit) : AdapterView.OnItemSelectedListener {
    val toCall = f
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        toCall(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}