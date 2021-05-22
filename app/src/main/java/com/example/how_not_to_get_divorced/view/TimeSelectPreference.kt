package com.example.how_not_to_get_divorced.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.example.how_not_to_get_divorced.R


class TimeSelectPreference(ctx: Context, attrs: AttributeSet) : DialogPreference(ctx, attrs) {

    fun getCurrentValue(): Int {
        return super.getPersistedInt(DEFAULT_VALUE)
    }

    fun saveValue(time: Int) {
        super.persistInt(time)
        notifyChanged()
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        super.onSetInitialValue(defaultValue)
        summary = formatSecondsOfDay(getCurrentValue())
    }

    companion object {
        fun formatSecondsOfDay(x : Int): String{
            val h = x / 3600
            val m = (x / 60) % 60
            return "${extraZeros(h)}:${extraZeros(m)}"
        }

        private fun extraZeros(x: Int): String{
            val s = x.toString()
            return if (s.length == 1) "0$s"
            else s
        }

        const val DEFAULT_VALUE = 8 * 3600
    }
}