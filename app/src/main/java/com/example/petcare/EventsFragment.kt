package com.example.petcare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.FragmentEventsBinding
import com.example.petcare.databinding.FragmentToDoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


private lateinit var auth: FirebaseAuth
private lateinit var databaseRef: DatabaseReference
private lateinit var navController: NavController
private lateinit var binding: FragmentEventsBinding
private var popupFragment:AddEventPopupFragment?=null
private lateinit var adapter: EventsAdapter
private lateinit var mList:MutableList<EventsData>


class EventsFragment : Fragment(), DialogNextBtnClickListener3,
    EventsAdapter.EventsAdapterClicksInterface {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.addEventBtn.setOnClickListener {

            if(popupFragment!=null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment = AddEventPopupFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,
                AddEventPopupFragment.TAG
            )
        }
    }

    private fun init(view: View) {

        navController= Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Events").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= EventsAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter= adapter

    }

    private fun getDataFromFirebase(){

        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(eventSnapShot in snapshot.children){
                    val eventText = eventSnapShot.key?.let {
                        EventsData(it,eventSnapShot.value.toString())
                    }
                    if (eventText!=null){
                        mList.add(eventText)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveEvent(event: String, eventEt: TextInputEditText) {

        databaseRef.push().setValue(event).addOnCompleteListener {
            if(it.isSuccessful){

                Toast.makeText(context, "Event saved successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            eventEt.text=null
            popupFragment!!.dismiss()
        }
    }

    override fun onUpdateEvent(eventsData: EventsData, eventEt: TextInputEditText) {

        val map= HashMap<String, Any>()
        map[eventsData.EventId] = eventsData.Event
        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "The event updated successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            eventEt.text=null
            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteEventBtnClicked(eventsData: EventsData) {
        databaseRef.child(eventsData.EventId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "Event deleted successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditEventBtnClicked(eventsData: EventsData) {

        if(popupFragment!=null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()

        popupFragment= AddEventPopupFragment.newInstance(eventsData.EventId, eventsData.Event)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager, AddEventPopupFragment.TAG)
    }

}