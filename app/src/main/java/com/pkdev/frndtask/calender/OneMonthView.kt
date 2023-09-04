package com.pkdev.frndtask.calender

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import java.util.*
import kotlin.collections.ArrayList


class OneMonthView(context: Context?, attrs: AttributeSet?, defStyle: Int) :
    LinearLayout(context, attrs, defStyle), View.OnClickListener {
    private val mContext: Context?

    var year = 0
        private set

    var month = 0
        private set
    private var dayCount: Int
    private var calType: Int
    private lateinit var onDayClickListener: OneDayView.OnDayClickListener

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    fun setCalType(calType: Int) {
        this.calType = calType
    }

    fun setOnDayClickListener(onDayClickListener: OneDayView.OnDayClickListener) {
        this.onDayClickListener = onDayClickListener
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    private var weeks: ArrayList<LinearLayout>? = null
    private var dayViews: ArrayList<OneDayView>? = null

    init {
        mContext = context
        orientation = VERTICAL
        dayCount = 0
        calType = 0

        //Create enough views in advance.
        if (weeks == null) {
            weeks = ArrayList(6) // Up to 6 weeks per month
            dayViews = ArrayList(42) // 7 days * 6 weeks = 42
            var ll: LinearLayout? = null
            for (i in 0..41) {
                if (i % 7 == 0) {
                    //Create a week layout
                    ll = LinearLayout(mContext)
                    val params = LayoutParams(LayoutParams.MATCH_PARENT, 0)
                    params.weight = 1f
                    ll.orientation = HORIZONTAL
                    ll.layoutParams = params
                    ll.weightSum = 7f
                    weeks!!.add(ll)
                }
                val params = LayoutParams(0, LayoutParams.MATCH_PARENT)
                params.weight = 1f
                val ov = mContext?.let { OneDayView(it) }
                if (ov != null) {
                    ov.layoutParams = params
                    ov.setOnClickListener(this)
                    ll?.addView(ov)
                    dayViews!!.add(ov)
                }


            }
        }

        //Preview
        if (isInEditMode) {
            val cal: Calendar = Calendar.getInstance()
            make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
        }
    }

    fun make(year: Int, month: Int) {
        if (this.year == year && this.month == month) {
            return
        }
        val makeTime = System.currentTimeMillis()
        this.year = year
        this.month = month


        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, 1)
        cal.setFirstDayOfWeek(Calendar.SUNDAY) //Specify Sunday as the start of the week
        val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK) //day of the week
        val maxOfMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH) //last days
        val oneDayDatas = ArrayList<OneDayData>()
        cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek) // Go to first day of the week

        /* add previous month */
        var seekDay: Int
        while (true) {
            seekDay = cal.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek == seekDay) break
            val one = OneDayData()
            one.dayType = OneDayData.DAY_TYPE_PREVIOUS
            one.day = cal
            oneDayDatas.add(one)
            //하루 증가
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        /* add this month */for (i in 0 until maxOfMonth) {
            val one = OneDayData()
            one.dayType = OneDayData.DAY_TYPE_NONE
            one.day = cal
            oneDayDatas.add(one)
            //하루 증가
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        /* add next month */while (true) {
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                val one = OneDayData()
                one.dayType = OneDayData.DAY_TYPE_NEXT
                one.day = cal
                oneDayDatas.add(one)
            } else {
                break
            }
            //increase by day
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        if (oneDayDatas.size == 0) return

        //clear all states
        removeAllViews()
        var count = 0
        for (oneday in oneDayDatas) {
            if (count % 7 == 0) {
                addView(weeks!![count / 7])
            }
            val ov = dayViews!![count]
            ov.setCalType(calType)
            ov.setDayClickListener(onDayClickListener)
            ov.day = oneday
            ov.setMsg("")
            ov.refresh()

            count++
        }
        dayCount = count

        /* Specify weight according to the number of notes*/this.weightSum = childCount.toFloat()


    }

    fun setDayTask(index: Int, isTask: Boolean){
        for (i in 0 until dayCount) {
            if (dayViews!![i].date == index) {
                dayViews!![i].refresh()
                dayViews!![i].setDateTask(isTask)
            }
        }
    }

    fun clearSelect() {
        for (i in 0 until dayCount) {
            dayViews!![i].refresh()
            dayViews!![i].setSelect(false)
        }
    }

    fun selectDay(index: Int) {
        for (i in 0 until dayCount) {
            dayViews!![i].refresh()
            dayViews!![i].setSelect(false)
        }
        for (i in 0 until dayCount) {
            if (dayViews!![i].date == index) {
                dayViews!![i].refresh()
                dayViews!![i].setSelect(true)
            }
        }
    }

    fun setItemDay(dates: ArrayList<String>) {
        for (i in 0 until dayCount) {
            dayViews!![i].setItem(false)
            for (j in 0 until dates.size) {
                if (dayViews!![i].date == dates[j].toInt()) {
                    dayViews!![i].setItem(true)
                    break
                }
            }
        }
    }


    override fun onClick(v: View?) {
        val ov = v as OneDayView?
    }

    companion object {
        private const val TAG = "OneMonthView"
        private const val NAME = "OneMonthView"
    }
}