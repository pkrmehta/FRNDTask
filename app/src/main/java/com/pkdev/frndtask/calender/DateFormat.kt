package com.pkdev.frndtask.calender

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    fun getCurrentDate(format: String?): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf =SimpleDateFormat(format, Locale.ENGLISH)
        return sdf.format(date)
    }

    fun getDate(format: String?, day: Int): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, day)
        return sdf.format(cal.time)
    }

    fun getDDate(date: String?, format: String?, day: Int): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val cal: Calendar = Calendar.getInstance()
        try {
            val newDate: Date = date?.let { sdf.parse(it) } as Date
            cal.time = newDate
            cal.add(Calendar.DATE, day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return sdf.format(cal.time)
    }

    fun getMDate(date: String?, format: String?, day: Int): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val cal: Calendar = Calendar.getInstance()
        try {
            val newDate: Date = date?.let { sdf.parse(it) } as Date
            cal.time = newDate
            cal.add(Calendar.MONTH, day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return sdf.format(cal.time)
    }

    fun getYDate(date: String?, format: String?, year: Int): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val cal: Calendar = Calendar.getInstance()
        try {
            val newDate: Date = date?.let { sdf.parse(it) } as Date
            cal.setTime(newDate)
            cal.add(Calendar.YEAR, year) //년 더하기
        } catch (e: ParseException) {
            // ComLog.d("DateFormat error-03");
//            e.printStackTrace();
        }
        return sdf.format(cal.getTime())
    }

    fun newFormat(date: String?, oldFormat: String?, newFormat: String?): String {
        var newDate = ""
        val old_format = SimpleDateFormat(oldFormat, Locale.ENGLISH)
        val new_format = SimpleDateFormat(newFormat, Locale.ENGLISH)
        try {
            val old_date: Date = date?.let { old_format.parse(it) } as Date
            newDate = new_format.format(old_date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newDate
    }

    fun isValidationDate(date: String?, format: String?): Boolean {
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            dateFormat.isLenient = false
            if (date != null) {
                dateFormat.parse(date)
            }
            true
        } catch (e: ParseException) {
            false
        }
    }
}