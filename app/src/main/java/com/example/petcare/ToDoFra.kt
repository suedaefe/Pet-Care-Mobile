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
import com.example.petcare.databinding.FragmentToDoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ToDoFra : Fragment(), DialogNextBtnClickListener, ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentToDoBinding
    private var popupFragment: AddToDoPopupFragment? =null
    private lateinit var todoadapter:ToDoAdapter
    private lateinit var mList :MutableList<ToDoData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
    }


    //butona basinca popup cikmasi icin
    private fun registerEvents() {
        binding.addTaskBtn.setOnClickListener {

            if(popupFragment!=null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()

            popupFragment = AddToDoPopupFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,
                AddToDoPopupFragment.TAG
            )
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager= LinearLayoutManager(context)

        mList= mutableListOf()
        todoadapter= ToDoAdapter(mList)
        todoadapter.setListener(this)
        binding.recyclerView.adapter = todoadapter

    }

    //firebase'e kaydettigimiz verileri cekmek icin, yeni veri, edit, delete vs.
    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(taskSnapShot in snapshot.children){
                    val todoTask = taskSnapShot.key?.let {
                        ToDoData(it, taskSnapShot.value.toString())
                    }
                    if (todoTask!=null ) {
                        mList.add(todoTask)
                    }
                }
                todoadapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    //veri kaydı icin
    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text= null
            popupFragment!!.dismiss()
        }

    }

    //veri guncellemesi icin
    override fun onUpdateTask(ToDoData: ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[ToDoData.TaskId]= ToDoData.Task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "updated successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text=null
            popupFragment!!.dismiss()
        }
    }

    //sil butonuna basınca silinmesi icin
    override fun onDeleteTaskBtnClicked(toDoData: ToDoData){

        databaseRef.child(toDoData.TaskId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    //edit butonuna basınca editlenmesi icin, popup'i eski veriyle beraber gosterecek
    override fun onEditTaskBtnClicked(toDoData: ToDoData){

        if(popupFragment!=null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()

        popupFragment= AddToDoPopupFragment.newInstance(toDoData.TaskId, toDoData.Task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager, AddToDoPopupFragment.TAG)


    }



}