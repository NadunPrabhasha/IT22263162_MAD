package com.example.todo1

import android.content.Context
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.todo1.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter


class TaskItemViewHolder(
    private val context: Context,
    private val bding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(bding.root)
{
    @RequiresApi(Build.VERSION_CODES.O)
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    fun bindTaskItem(taskItem: TaskItem)
    {
        bding.name.text = taskItem.name

        if (taskItem.isCompleted()){
            bding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            bding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        bding.completeButton.setImageResource(taskItem.imageResource())
        bding.completeButton.setColorFilter(taskItem.imageColor(context))

        bding.completeButton.setOnClickListener{
            clickListener.completeTaskItem(taskItem)
        }
        bding.taskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }

        if(taskItem.dueTime() != null)
            bding.dueTime.text = timeFormat.format(taskItem.dueTime())
        else
            bding.dueTime.text = ""
    }
}