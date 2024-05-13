package com.example.todo1

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.todo1.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment()
{
    private lateinit var bding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (taskItem != null)
        {
            bding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            bding.name.text = editable.newEditable(taskItem!!.name)
            bding.desc.text = editable.newEditable(taskItem!!.desc)
            if(taskItem!!.dueTime() != null)
            {
                dueTime = taskItem!!.dueTime()!!
                updateTimeButtonText()
            }
        }
        else
        {
            bding.taskTitle.text = "New Task"
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        bding.saveButton.setOnClickListener {
            saveAction()
        }
        bding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText() {
        bding.timePickerButton.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return bding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveAction()
    {
        val name = bding.name.text.toString()
        val desc = bding.desc.text.toString()
        val dueTimeString = if(dueTime == null) null else TaskItem.timeFormatter.format(dueTime)
        if(taskItem == null)
        {
            val newTask = TaskItem(name,desc, dueTimeString,null)
            taskViewModel.addTaskItem(newTask)
        }
        else
        {
            taskItem!!.name = name
            taskItem!!.desc = name
            taskItem!!.dueTimeString = dueTimeString

            taskViewModel.updateTaskItem(taskItem!!)
        }
        bding.name.setText("")
        bding.desc.setText("")
        dismiss()
    }

}