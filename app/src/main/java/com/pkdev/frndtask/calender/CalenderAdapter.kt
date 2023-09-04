package com.pkdev.frndtask.calender

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import java.util.*
import kotlin.collections.ArrayList


class CalendarAdapter(context: Context, onDayClickListener: OneDayView.OnDayClickListener) :
    PagerAdapter(), OnPageChangeListener {
    private val mContext: Context
    private val monthViews: ArrayList<OneMonthView>

    private val BASE_CAL: Calendar
    private var previousPosition = 0
    private var mCurrentPosition = 0
    private var mCalType: Int

    interface OnMonthChangeListener {
        fun onChange(year: Int, month: Int, monthViews: OneMonthView)
    }

    private val dummyListener: OnMonthChangeListener = object : OnMonthChangeListener {
        override fun onChange(year: Int, month: Int, monthViews: OneMonthView) {}
    }
    private var listener = dummyListener

    init {
        mContext = context
        val base: Calendar = Calendar.getInstance()
        base.set(BASE_YEAR, BASE_MONTH, 1)
        BASE_CAL = base
        mCalType = CAL_TYPE_DEFAULT
        monthViews = ArrayList()
        for (i in 0 until PAGES) {
            monthViews.add(OneMonthView(context))
            monthViews[i].setCalType(mCalType)
            monthViews[i].setOnDayClickListener(onDayClickListener)
        }
    }

    fun setCalType(calType: Int) {
        mCalType = calType
        for (i in monthViews.indices) {
            monthViews[i].setCalType(mCalType)
        }
    }

    private fun getYearMonth(position: Int): YearMonth {
        val cal: Calendar = BASE_CAL.clone() as Calendar
        cal.add(Calendar.MONTH, position - BASE_POSITION)
        return YearMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
    }

    fun getPosition(year: Int, month: Int): Int {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, 1)
        return BASE_POSITION + howFarFromBase(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
    }

    fun setOnMonthChangeListener(onMonthChangeListener: OnMonthChangeListener) {
        listener = onMonthChangeListener
    }

    private fun howFarFromBase(year: Int, month: Int): Int {
        val disY = (year - BASE_YEAR) * 12
        val disM = month - BASE_MONTH
        return disY + disM
    }

    fun test(index: Int) {
        for (i in 0 until PAGES) {
            monthViews[i].selectDay(index)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        var position = position
        val howFarFromBase = position - BASE_POSITION
        val cal: Calendar = BASE_CAL.clone() as Calendar
        cal.add(Calendar.MONTH, howFarFromBase)

        position %= PAGES

        container.addView(monthViews[position])
        monthViews[position].make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))

        var copyMonthViews: Array<OneMonthView?>? = null
        copyMonthViews = arrayOfNulls<OneMonthView>(monthViews.size)
        for (i in monthViews.indices) {
            copyMonthViews[i] = monthViews[i]
        }

        return monthViews[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return PAGES * LOOPS
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager.SCROLL_STATE_IDLE -> {}
            ViewPager.SCROLL_STATE_DRAGGING -> //            	Log.i(TAG, "SCROLL_STATE_DRAGGING");
                previousPosition = mCurrentPosition
            ViewPager.SCROLL_STATE_SETTLING -> {}
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        if (previousPosition != position) {
            previousPosition = position
            val ym: YearMonth = getYearMonth(position)
            val howFarFromBase = position % PAGES
            listener.onChange(ym.year, ym.month, monthViews[howFarFromBase])
        }
    }

    override fun onPageSelected(position: Int) {
        mCurrentPosition = position
    }

    companion object {
        var CAL_TYPE_DEFAULT = 0
        const val BASE_YEAR = 2015
        const val BASE_MONTH: Int = Calendar.JANUARY
        const val PAGES = 5
        const val LOOPS = 1000
        const val BASE_POSITION = PAGES * LOOPS / 2
    }
}