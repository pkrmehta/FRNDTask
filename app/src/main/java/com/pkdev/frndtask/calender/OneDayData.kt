package com.pkdev.frndtask.calender

import java.util.*

class OneDayData {
    var cal: Calendar

    var msg: CharSequence = ""
    var dayType: Int
    var isFocus: Boolean

    init {
        cal = Calendar.getInstance()
        isFocus = false
        dayType = DAY_TYPE_NONE
    }


    fun setDay(year: Int, month: Int, day: Int) {
        cal = Calendar.getInstance()
        cal.set(year, month, day)
    }

    var day: Calendar
        get() = cal
        set(cal) {
            this.cal = cal.clone() as Calendar
        }

    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    operator fun get(field: Int): Int {
        return cal.get(field)
    }

    companion object {
        const val DAY_TYPE_PREVIOUS = 0
        const val DAY_TYPE_NONE = 1
        const val DAY_TYPE_NEXT = 2
    }
}