package com.bxtruong.huyproject

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bxtruong.huyproject.databinding.ActivityMainBinding
import com.bxtruong.huyproject.helper.Helper
import com.bxtruong.huyproject.model.ExaminationRoom
import com.bxtruong.huyproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val examinationRooms = ArrayList<ExaminationRoom>()
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.main = this
        binding.checkTeacher = Helper.checkTeacher(this)

        user = Helper.readUser(this)
        binding.user = Helper.readUser(this)
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
                    if (user.teacher) {
                        if (category!!.teacher1 == user.full_name || category.teacher2 == user.full_name)
                            examinationRooms.add(category)
                    } else {
                        examinationRooms.add(category!!)
                    }
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
                        openUpdate(item)
                    }

                    override fun delete(item: ExaminationRoom) {
                        dialogDelete(item)
                    }

                },
                Helper.checkTeacher(this)
            )

        binding.rcCategory.adapter = adapter
    }

    fun addExaminationRoom() {
        val intent = Intent(this, FormActivity::class.java).apply {
            putExtra("check", "Tạo")
            putExtra("id", "")

        }
        startActivity(intent)
    }

    fun openUpdate(examinationRoom: ExaminationRoom ) {
        val intent = Intent(this, FormActivity::class.java).apply {
            putExtra("check", "Chỉnh Sửa")
            putExtra("id", examinationRoom.id)
        }
        startActivity(intent)
    }

    fun dialogDelete(examinationRoom: ExaminationRoom) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Xóa phòng thi ${examinationRoom.nameRoom}")
        builder.setMessage("Bạn chắc chứ ?")

        builder.setPositiveButton("OK") { dialog, _ ->
            FirebaseDatabase.getInstance().reference.child("Examination").child(examinationRoom.id)
                .removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        dialog.dismiss()
                    }
                }
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    fun openSignUp() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    fun logOut() {
        val intent = Intent(this, LoginActivity2::class.java)
        startActivity(intent)
        Helper.writeUser(this, User("", "", "", "", "", true))
    }
}