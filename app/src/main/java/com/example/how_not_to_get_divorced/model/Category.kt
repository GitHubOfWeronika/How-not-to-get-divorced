package com.example.how_not_to_get_divorced.model

import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.ui.customSpinner.CustomItem

/**
 * Enum of every category for alarms
 */
enum class Category(val id: Int, val categoryName: String, val image: Int) : CustomItem{
    HOME(1, "Home", R.drawable.alarm_icon);

    override fun getImg(): Int = image
    override fun getText(): String = categoryName

    companion object {
        val CATEGORIES_LIST = values()

        fun getById(id: Int): Category?{
            for (cat in values()){
                if (id == cat.id){
                    return cat
                }
            }
            return null
        }
    }
}