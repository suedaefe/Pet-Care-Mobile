package com.example.petcare

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.petcare.databinding.FragmentAddToDoPopupBinding
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalTime
import java.util.*


class AddToDoPopupFragment : DialogFragment() , DatePickerDialog.OnDateSetListener{

    private lateinit var binding: FragmentAddToDoPopupBinding
    private lateinit var listener : DialogNextBtnClickListener
    private var toDoData: ToDoData?= null
    private var dueTime: LocalTime? = null

    fun setListener(listener: DialogNextBtnClickListener){
        this.listener= listener
    }


    companion object{
        const val TAG = "AddToDoPopupFragment"
        @JvmStatic
        //kullanici edit yaparken girdigi eski veriyi kullaniciya gostermek icin
        fun newInstance(taskId: String, task: String)= AddToDoPopupFragment().apply {
            arguments= Bundle().apply {
                putString("TaskId", taskId)
                putString("Task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentAddToDoPopupBinding.inflate(inflater,container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null)
        //eger veri varsa yani kullanici edit islemi yapmak istiyorsa
        {
            toDoData= ToDoData(arguments?.getString("TaskId", "Task").toString(), arguments?.getString("Task").toString())
        }
        binding.todoEt.setText(toDoData?.Task)

        registerevents()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerevents() {
        binding.todoDateBtn.setOnClickListener {
            //tarih secmek icin
            showDatePicker()
        }
        binding.todoTimeBtn.setOnClickListener {
            //saat secmek icin
            showTimePicker()
        }
        binding.todoNextBtn.setOnClickListener {

            val todoTask = binding.textViewDate.text.toString()+" "+ binding.textViewTime.text.toString()+" "+
                    binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()) {
                if (toDoData==null) {
                    //yeni veri ekleniyorsa
                    listener.onSaveTask(todoTask, binding.todoEt)
                }
                else{
                    //mevcut veri editleniyorsa
                    toDoData?.Task= todoTask
                    listener.onUpdateTask(toDoData!!, binding.todoEt)
                }
            } else {
                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()

            }
            binding.todoClose.setOnClickListener {
                dismiss()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePicker() {
        //kullanici bir saat secmediyse o anki saate ayarliyor
        if(dueTime == null)
            dueTime = LocalTime.now()
        //Kullanicinin sectigi saati dueTime'a atiyor
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            //secilen saati butontext'e ayarlamak icin
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText() {
        binding.todoTimeBtn.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
        binding.textViewTime.text= String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }

    private fun showDatePicker() {
        //kullanicidan get ile veri aliyoruz
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textViewDate.setText("" + dayOfMonth + "/" + month + "/" + year)
                binding.todoDateBtn.setText("" + dayOfMonth + "/" + month + "/" + year)
            },
            year,
            month,
            dayOfMonth
        )
        datePicker.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //alinan verileri set ediyoruz
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

}
interface DialogNextBtnClickListener{
    fun onSaveTask(todo: String, todoEt: TextInputEditText)

    fun onUpdateTask(ToDoData: ToDoData, todoEt: TextInputEditText)
}