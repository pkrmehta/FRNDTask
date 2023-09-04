package com.pkdev.frndtask


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkdev.frndtask.adapter.TaskAdapter
import com.pkdev.frndtask.adapter.TaskClickListener
import com.pkdev.frndtask.calender.CalendarAdapter
import com.pkdev.frndtask.calender.DateFormat
import com.pkdev.frndtask.calender.OneDayView
import com.pkdev.frndtask.calender.OneMonthView
import com.pkdev.frndtask.databinding.ActivityMainBinding
import com.pkdev.frndtask.model.StoreTaskRequest
import com.pkdev.frndtask.model.Task
import com.pkdev.frndtask.model.TaskRequest
import com.pkdev.frndtask.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), TaskClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mCurrentYear: String
    private lateinit var mCurrentMonth: String
    private lateinit var mCurrentDate: String

    private lateinit var adapter: CalendarAdapter
    private lateinit var mOneMonthView: OneMonthView

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var dialog: Dialog

    private lateinit var taskList: ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        mCurrentYear = DateFormat.getCurrentDate("yyyy")
        mCurrentMonth = DateFormat.getCurrentDate("MM")
        mCurrentDate = DateFormat.getCurrentDate("yyyyMMdd")
        setView()


        setObservers()


    }

    override fun onResume() {
        super.onResume()

        dialog = Dialog(this@MainActivity)
        binding.btnNewTask.setOnClickListener{
            dialog.setContentView(R.layout.dialog_new_task)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val textDate = dialog.findViewById<AppCompatTextView>(R.id.text_date)
            val etTitle = dialog.findViewById<EditText>(R.id.et_title)
            val etDesc = dialog.findViewById<EditText>(R.id.et_desc)
            val btnAdd = dialog.findViewById<AppCompatButton>(R.id.btn_add_task)

            textDate.text = getDateToViewFormat(mCurrentDate)

            btnAdd.setOnClickListener {
                addTask(textDate, etTitle, etDesc, dialog)
            }
            dialog.show()
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)


        taskViewModel.getTask()
    }

    private fun updateRecyclerView(list: ArrayList<Task>){
        if(list.size >0) {
            binding.taskRecyclerView.visibility = View.VISIBLE
            binding.noTaskView.visibility = View.GONE
            binding.taskRecyclerView.apply {
                adapter = TaskAdapter(this@MainActivity, list, this@MainActivity)
            }
        }
        else{
            binding.taskRecyclerView.visibility = View.GONE
            binding.noTaskView.visibility = View.VISIBLE
        }

    }

    private fun addTask(textDate: AppCompatTextView, etTitle: EditText, etDesc: EditText, dialog: Dialog) {
        val title = etTitle.text.toString()
        val desc = etDesc.text.toString()
        val date = textDate.text.toString()
        if(title.isEmpty()){
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
            return
        }
        if(desc.isEmpty()){
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show()
            return
        }

        val payload = StoreTaskRequest(514, TaskRequest(title,desc,date))
        taskViewModel.storeTask(payload)



    }




    private fun setObservers() {
        taskViewModel.getTask().observe(this){
            if(it!=null) {
                taskList = it.tasks as ArrayList<Task>
                binding.taskProgressbar.visibility = View.GONE
                val updateTaskList = kotlin.collections.ArrayList<Task>()
                for(t in taskList){
                    val date = getDateToCalFormat(t)
                    mOneMonthView.setDayTask(date.toInt(),true)
                    if(date == mCurrentDate)
                        updateTaskList.add(t)
                }
                updateRecyclerView(updateTaskList)
            }
            else{
                binding.taskRecyclerView.visibility = View.GONE
                binding.noTaskView.visibility = View.VISIBLE
                binding.taskProgressbar.visibility = View.GONE
            }
        }

        taskViewModel.storeTaskResponse.observe(this){
            if(dialog.isShowing)
                dialog.dismiss()
            if(it!=null){
                if(it.status.toLowerCase() == "success"){
                    Toast.makeText(this, "Task Successfully added", Toast.LENGTH_SHORT).show()
                    taskViewModel.getTask()
                }
                else{
                    Toast.makeText(this, "Task addition failed", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Task addition failed", Toast.LENGTH_SHORT).show()
            }
        }

        taskViewModel.deleteTaskResponse.observe(this){
            if(it!=null){
                if(it.status.toLowerCase() == "success"){
                    Toast.makeText(this, "Task Successfully deleted", Toast.LENGTH_SHORT).show()
                    taskViewModel.getTask()
                }
                else{
                    Toast.makeText(this, "Task deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Task deletion failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setView() {
        binding.btnPrev.setOnClickListener(onClickListener)
        binding.btnFrwd.setOnClickListener(onClickListener)
        binding.textMonthYear.text = "$mCurrentMonth,$mCurrentYear"
        adapter = CalendarAdapter(this@MainActivity, onDayClickListener)
        adapter.setOnMonthChangeListener(onMonthChangeListener)
        adapter.setCalType(0)
        binding.datesViewpager.adapter = adapter
        binding.datesViewpager.setOnPageChangeListener(adapter)
        binding.datesViewpager.currentItem = adapter.getPosition(mCurrentYear.toInt(), mCurrentMonth.toInt() - 1)
        binding.datesViewpager.offscreenPageLimit = 1
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        private var date = ""
        private var prevDate = ""
        private var year = 0
        private var month = 0
        override fun onClick(view: View) {
            when (view.getId()) {
                binding.btnPrev.id -> {
                    date = mCurrentYear + mCurrentMonth
                    prevDate = DateFormat.getMDate(date, "yyyyMM", -1)
                    year = DateFormat.newFormat(prevDate, "yyyyMM", "yyyy").toInt()
                    month = DateFormat.newFormat(prevDate, "yyyyMM", "MM").toInt()
                    binding.datesViewpager.currentItem = adapter.getPosition(year, month - 1)
                }
                binding.btnFrwd.id -> {
                    date = mCurrentYear + mCurrentMonth
                    prevDate = DateFormat.getMDate(date, "yyyyMM", 1)
                    year = DateFormat.newFormat(prevDate, "yyyyMM", "yyyy").toInt()
                    month = DateFormat.newFormat(prevDate, "yyyyMM", "MM").toInt()
                    binding.datesViewpager.currentItem = adapter.getPosition(year, month - 1)
                }
            }
        }
    }

    private val onMonthChangeListener: CalendarAdapter.OnMonthChangeListener = object :
        CalendarAdapter.OnMonthChangeListener {
        override fun onChange(year: Int, month: Int, monthViews: OneMonthView) {
            mOneMonthView = monthViews
            mCurrentYear = year.toString()
            mCurrentMonth = (month + 1).toString()

            if(this@MainActivity::taskList.isInitialized) {
                for (t in taskList) {
                    val date = getDateToCalFormat(t)
                    mOneMonthView.setDayTask(date.toInt(), true)
                }
            }

            val c = Calendar.getInstance()
            val monthDate = SimpleDateFormat("MMMM", Locale.ENGLISH)
            c[Calendar.MONTH] = month
            binding.textMonthYear.text = "${monthDate.format(c.time)}, $year"
            adapter.test(mCurrentDate.toInt())

        }
    }

    private val onDayClickListener: OneDayView.OnDayClickListener =
        object : OneDayView.OnDayClickListener {
            override fun onClick(view: View) {
                mCurrentDate = java.lang.String.valueOf(view.id)
                val updateTaskList = kotlin.collections.ArrayList<Task>()
                for(t in taskList){
                    if(getDateToCalFormat(t) == mCurrentDate)
                        updateTaskList.add(t)
                }
                updateRecyclerView(updateTaskList)
                adapter.test(mCurrentDate.toInt())
                mOneMonthView.selectDay(view.id)
            }
        }

    private fun getDateToCalFormat(t: Task): String{
        val d = t.taskDetail.date.split("/")
        var date = ""
        for(i in d.size-1 downTo 0){
            date += d[i]
        }
        return date
    }

    private fun getDateToViewFormat(date: String): String{
        var formatDate = ""
        formatDate += date.substring(6,8)
        formatDate += "/"
        formatDate += date.substring(4,6)
        formatDate += "/"
        formatDate += date.substring(0,4)

        return formatDate
    }

    override fun onTaskClick(taskId: Int) {
        taskViewModel.deleteTask(taskId)
    }
}