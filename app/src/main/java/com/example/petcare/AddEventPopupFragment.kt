package com.example.petcare

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.petcare.databinding.FragmentAddEventPopupBinding
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalTime
import java.util.*


class AddEventPopupFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentAddEventPopupBinding
    private lateinit var listener2: DialogNextBtnClickListener3
    private var eventsData: EventsData?= null
    private var Time: LocalTime? = null

    fun setListener(listener2: DialogNextBtnClickListener3){

        this.listener2=listener2

    }

    companion object{

        const val TAG = "AddEventPopupFragment"
        @JvmStatic
        fun newInstance(eventId: String, event: String)= AddEventPopupFragment().apply {
            arguments= Bundle().apply {
                putString("EventId", eventId)
                putString("Event", event)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEventPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!=null){
            eventsData= EventsData(arguments?.getString("EventId").toString(),
                arguments?.getString("Event").toString())
            binding.eventEt.setText(eventsData?.Event)
        }

        registerEvents()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerEvents() {

        binding.eventDateBtn.setOnClickListener {
            showDatePicker()
        }
        binding.eventTimeBtn.setOnClickListener {
            showTimePicker()
        }

        binding.foodBtn.setOnClickListener {
            binding.textViewEvent.text= "FOOD    "
            Toast.makeText(context, "Selected Item : FOOD", Toast.LENGTH_SHORT).show()
        }
        binding.treatBtn.setOnClickListener {
            binding.textViewEvent.text= "TREAT   "
            Toast.makeText(context, "Selected Item : TREAT", Toast.LENGTH_SHORT).show()
        }
        binding.waterBtn.setOnClickListener {
            binding.textViewEvent.text= "WATER   "
            Toast.makeText(context, "Selected Item : WATER", Toast.LENGTH_SHORT).show()
        }
        binding.brushingBtn.setOnClickListener {
            binding.textViewEvent.text= "BRUSHING"
            Toast.makeText(context, "Selected Item : GROOMING", Toast.LENGTH_SHORT).show()
        }
        binding.toothBrushingBtn.setOnClickListener {
            binding.textViewEvent.text= "TOOTH  "
            Toast.makeText(context, "Selected Item : TOOTH-BRUSHING", Toast.LENGTH_SHORT).show()
        }
        binding.medicineBtn.setOnClickListener {
            binding.textViewEvent.text= "MEDICINE"
            Toast.makeText(context, "Selected Item : MEDICINE", Toast.LENGTH_SHORT).show()
        }
        binding.trainingBtn.setOnClickListener {
            binding.textViewEvent.text= "TRAINING"
            Toast.makeText(context, "Selected Item : TRAINING", Toast.LENGTH_SHORT).show()
        }
        binding.walkingBtn.setOnClickListener {
            binding.textViewEvent.text= "WALKING "
            Toast.makeText(context, "Selected Item : WALKING", Toast.LENGTH_SHORT).show()
        }
        binding.sleepingBtn.setOnClickListener {
            binding.textViewEvent.text= "SLEEPING"
            Toast.makeText(context, "Selected Item : SLEEPING", Toast.LENGTH_SHORT).show()
        }
        binding.eventNextBtn.setOnClickListener {
            val eventText= binding.textViewEvent.text.toString()+"          "+binding.textViewDate.text.toString()+"   "+ binding.textViewTime.text.toString()+" "+binding.eventEt.text.toString()
            if(eventText.isNotEmpty()){
                if(eventsData==null){
                    listener2.onSaveEvent(eventText, binding.eventEt)
                }
                else{
                    //this is for if the user is editing the event
                    eventsData?.Event= eventText
                    listener2.onUpdateEvent(eventsData!!, binding.eventEt)
                }

            }
            else{
                Toast.makeText(context, "Please choose an event", Toast.LENGTH_SHORT).show()
            }
        }
        binding.eventClose.setOnClickListener {
            dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePicker() {
        if(Time == null)
            Time = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            Time = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, Time!!.hour, Time!!.minute, true)
        dialog.setTitle("Time")
        dialog.show()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText() {
        binding.eventTimeBtn.text = String.format("%02d:%02d",Time!!.hour,Time!!.minute)
        binding.textViewTime.text= String.format("%02d:%02d",Time!!.hour,Time!!.minute)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textViewDate.setText("" + dayOfMonth + "/" + month + "/" + year)
                binding.eventDateBtn.setText("" + dayOfMonth + "/" + month + "/" + year)
            },
            year,
            month,
            dayOfMonth
        )
        datePicker.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

}
interface DialogNextBtnClickListener3{
    fun onSaveEvent(event: String, eventEt: TextInputEditText)
    fun onUpdateEvent(eventsData: EventsData, eventEt: TextInputEditText)
}