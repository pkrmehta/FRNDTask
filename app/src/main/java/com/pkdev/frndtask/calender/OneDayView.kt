package com.pkdev.frndtask.calender

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.pkdev.frndtask.R
import com.pkdev.frndtask.model.Task
import java.util.*
import kotlin.collections.ArrayList


class OneDayView : RelativeLayout {

    interface OnDayClickListener {
        fun onClick(view: View)
    }

    private lateinit var oneday_topGroup: RelativeLayout
    private lateinit var rl_onday_day: RelativeLayout
    private lateinit var ll_onday_day: LinearLayout
    private lateinit var onday_item: View

    private lateinit var tv_day: TextView
    private lateinit var one: OneDayData
    private lateinit var onDayClickListener: OnDayClickListener
    private val cal: Calendar = Calendar.getInstance()
    var date = 0
        private set
    private var calType = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        val v: View = View.inflate(context, R.layout.include_calender_oneday, this)
        oneday_topGroup = v.findViewById(R.id.oneday_topGroup)
        rl_onday_day = v.findViewById(R.id.rl_onday_day)
        ll_onday_day = v.findViewById(R.id.ll_onday_day)
        onday_item = v.findViewById(R.id.view_onday_item) as View
        tv_day = v.findViewById(R.id.tv_onday_day)
        one = OneDayData()
        calType = 0
    }

    var day: OneDayData
        get() = one
        set(cal) {
            one.day = (cal.cal.clone() as Calendar)
        }


    fun setMsg(msg: String?) {
        one.msg = msg!!
    }

    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    operator fun get(field: Int): Int {
        return one[field]
    }

    fun setCalType(calType: Int) {
        this.calType = calType
    }

    fun setSelect(isSelect: Boolean) {
        if (isSelect) {
            rl_onday_day.setBackgroundResource(R.drawable.round_bg007caa_5dp)
            if(tv_day.currentTextColor != Color.parseColor("#d9ab2e"))
                tv_day.setTextColor(-0x1)
        } else {
            rl_onday_day.setBackgroundResource(0)
            setDayColor()
        }
    }

    fun setDateTask(isTask: Boolean){
        if (isTask) {
            tv_day.setTextColor(Color.parseColor("#d9ab2e"))
        } else {
            setDayColor()
        }
    }

    fun setItem(isVisible: Boolean) {
        if (isVisible) {
            onday_item.visibility = VISIBLE
        } else {
            onday_item.visibility = GONE
        }
    }

    fun setDayClickListener(onDayClickListener: OnDayClickListener) {
        this.onDayClickListener = onDayClickListener
    }

    private fun setDayColor() {
        if(tv_day.currentTextColor != Color.parseColor("#d9ab2e")) {
            if (one[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
                when (one.dayType) {
                    OneDayData.DAY_TYPE_PREVIOUS -> tv_day.setTextColor(0x4D3273AC)
                    OneDayData.DAY_TYPE_NONE -> tv_day.setTextColor(-0xcd8c54)
                    OneDayData.DAY_TYPE_NEXT -> tv_day.setTextColor(0x4D3273AC)
                }
            } else if (one[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
                when (one.dayType) {
                    OneDayData.DAY_TYPE_PREVIOUS -> tv_day.setTextColor(0x4DDE5757)
                    OneDayData.DAY_TYPE_NONE -> tv_day.setTextColor(-0x21a8a9)
                    OneDayData.DAY_TYPE_NEXT -> tv_day.setTextColor(0x4DDE5757)
                }
            } else {
                when (one.dayType) {
                    OneDayData.DAY_TYPE_PREVIOUS -> tv_day.setTextColor(0x4D575656)
                    OneDayData.DAY_TYPE_NONE -> tv_day.setTextColor(-0xa8a9aa)
                    OneDayData.DAY_TYPE_NEXT -> tv_day.setTextColor(0x4D575656)
                }
            }
        }
    }


    fun refresh() {
        rl_onday_day.setBackgroundResource(0)
        setDayColor()

        date = (one[Calendar.YEAR] * 10000
                + (one[Calendar.MONTH] + 1) * 100 + one[Calendar.DAY_OF_MONTH])
        val current: Int = DateFormat.getCurrentDate("yyyyMMdd").toInt()
        oneday_topGroup.id = date
        oneday_topGroup.setOnClickListener { v ->

            when (calType) {
                0 ->                             //default
                    onDayClickListener.onClick(v)
                1 ->                             //not past
                    if (current > date) {
                        Toast.makeText(context, "Past dates cannot be selected.", Toast.LENGTH_SHORT).show()
                    } else if (current == date) {
                        Toast.makeText(context, "You can't choose today.", Toast.LENGTH_SHORT).show()
                    } else {
                        onDayClickListener.onClick(v)
                    }
                2 ->                             //not future
                    // 20191204 <= 20191203
                    if (current < date) {
                        Toast.makeText(context, "You cannot select a future date.", Toast.LENGTH_SHORT).show()
                    } else {
                        onDayClickListener.onClick(v)
                    }
            }

        }
        tv_day.text = one[Calendar.DAY_OF_MONTH].toString()
        setDayColor()
        if (one.isFocus) {
            tv_day.text = "djal"
        }
        val linear: LinearLayout? = null
        val linears = ArrayList<LinearLayout>()
        ll_onday_day.removeAllViews()
        for (i in linears.size - 1 downTo -1 + 1) ll_onday_day.addView(linears[i])
    }

}