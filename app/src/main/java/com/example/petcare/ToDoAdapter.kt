package com.example.petcare

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.TaskItemCellBinding

class ToDoAdapter(private val List:MutableList<ToDoData>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private lateinit var listener : ToDoAdapterClicksInterface

    fun setListener(listener: ToDoAdapterClicksInterface){
        this.listener= listener
    }

    inner class ToDoViewHolder(val binding: TaskItemCellBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        //edit ve delete butonlari task'i tutacak olan kutucukta oldugu icin binding'i ona ayarliyoruz
        val binding = TaskItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

        with(holder){
            with(List[position]){
                binding.todoTask.text= this.Task

                binding.deleteTask.setOnClickListener {
                    listener.onDeleteTaskBtnClicked(this)
                }
                binding.editTask.setOnClickListener {
                    listener.onEditTaskBtnClicked(this)
                }
                //butona basinca yazinin ustunu cizmesi icin
                binding.taskcompleteButton.setOnClickListener {
                    binding.todoTask.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    interface ToDoAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(toDoData: ToDoData){


        }
        fun onEditTaskBtnClicked(toDoData: ToDoData){

        }

    }
}