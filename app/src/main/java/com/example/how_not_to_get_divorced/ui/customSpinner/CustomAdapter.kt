package com.example.how_not_to_get_divorced.ui.customSpinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.how_not_to_get_divorced.R

/**
 * Adapter for custom spinner with icons
 *
 * ctx - context
 * list - list of elements to display
 */
class CustomAdapter(ctx: Context, list: List<CustomItem>) : ArrayAdapter<CustomItem>(ctx, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = when(convertView){
            null -> LayoutInflater.from(context).inflate(R.layout.category_spinner_layout, parent, false)
            else -> convertView
        }
        val item : CustomItem? = getItem(position)
        if (item != null){
            val image = convertView.findViewById<ImageView>(R.id.customSpinnerIV)
            val text = convertView.findViewById<TextView>(R.id.customSpinnerTV)
            image.setImageResource(item.getImg())
            text.text = item.getText()
        }
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = when(convertView){
            null -> LayoutInflater.from(context).inflate(R.layout.category_dropdown_layout, parent, false)
            else -> convertView
        }
        val item : CustomItem? = getItem(position)
        if (item != null){
            val image = convertView.findViewById<ImageView>(R.id.customDropdownIV)
            val text = convertView.findViewById<TextView>(R.id.customDropdownTV)
            image.setImageResource(item.getImg())
            text.text = item.getText()
        }
        return convertView
    }
}