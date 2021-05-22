package com.bxtruong.huyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bxtruong.huyproject.databinding.ActivityLogin2Binding
import com.bxtruong.huyproject.databinding.ActivityMainBinding
import com.bxtruong.huyproject.helper.Helper
import com.bxtruong.huyproject.model.ExaminationRoom
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.Normalizer

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val examinationRooms = ArrayList<ExaminationRoom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.main = this
        binding.checkTeacher= Helper.checkTeacher(this)
        initData()
    }

    private fun initData() {
        FirebaseDatabase.getInstance().reference.child("Examination").addValueEventListener(object :
                ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                examinationRooms.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val category = snapshot.getValue(ExaminationRoom::class.java)
                    examinationRooms.add(category!!)
                }
                if (examinationRooms.isEmpty()) {
                    binding.listIsEmpty = true
                } else {
                    binding.listIsEmpty = false
                    initRecyclerView()
                }
            }
        })
    }

    private fun initRecyclerView() {
        val adapter =
                AD(
                        examinationRooms,
                        object : ClickExamination {
                            override fun onClick(item: ExaminationRoom) {
                            }

                            override fun update(item: ExaminationRoom) {
                            }

                            override fun delete(item: ExaminationRoom) {
                            }

                        },
                        Helper.checkTeacher(this)
                )

        binding.rcCategory.adapter = adapter
    }

    fun addExaminationRoom() {
        startActivity(Intent(this, FormActivity::class.java))
    }
}