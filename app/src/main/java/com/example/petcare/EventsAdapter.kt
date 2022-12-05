package com.example.petcare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.EventItemCellBinding

class EventsAdapter(private val eventList: MutableList<EventsData>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var listener: EventsAdapterClicksInterface? = null
    fun setListener(listener: EventsAdapterClicksInterface) {
        this.listener = listener

    }

    inner class EventViewHolder(val binding: EventItemCellBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            EventItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        with(holder) {
            with(eventList[position]) {
                binding.eventname.text = this.Event

                binding.deleteEvent.setOnClickListener {
                    listener?.onDeleteEventBtnClicked(this)
                }
                binding.editEvent.setOnClickListener {
                    listener?.onEditEventBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    interface EventsAdapterClicksInterface {

        fun onDeleteEventBtnClicked(eventsData: EventsData) {

        }

        fun onEditEventBtnClicked(eventsData: EventsData) {


        }

    }
}