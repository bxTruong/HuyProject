package com.bxtruong.huyproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bxtruong.huyproject.databinding.ActivityFormBinding
import com.bxtruong.huyproject.helper.Helper
import com.bxtruong.huyproject.model.ExaminationRoom
import com.bxtruong.huyproject.model.NotificationData
import com.bxtruong.huyproject.model.PushNotification
import com.bxtruong.huyproject.model.User
import com.bxtruong.huyproject.notification.InitRetrofit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

const val TOPIC = "/topics/myTopic2"

class FormActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var binding: ActivityFormBinding
    private val teachersName = ArrayList<String>()
    private val teachers = ArrayList<User>()
    private var progressDialog: ProgressDialog? = null

    lateinit var idUpdate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form)
        binding.formExaminationFRG = this
        binding.date = "Trống"
        binding.start = "Trống"
        binding.finish = "Trống"
        FirebaseDatabase.getInstance().reference.child("Account")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    teachersName.clear()
                    for (snapshot: DataSnapshot in p0.children) {
                        val user = snapshot.getValue(User::class.java)
                        teachersName.add(user!!.full_name)
                        teachers.add(user)
                    }
                    autoCompleteTextView()
                }
            })

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.check = intent.getStringExtra("check")
        idUpdate = intent.getStringExtra("id")!!


        if (intent.getStringExtra("check") != "Tạo") {
            FirebaseDatabase.getInstance().reference.child("Examination").child(idUpdate)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val examinationRoom = p0.getValue(ExaminationRoom::class.java)!!
                        binding.edtNameForm.setText(examinationRoom.nameRoom)
                        binding.edtTeacher1Form.setText(examinationRoom.teacher1)
                        binding.edtTeacher2Form.setText(examinationRoom.teacher2)
                        binding.edtExamForm.setText(examinationRoom.exam)
                    }
                })
        }

    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = InitRetrofit.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.e("SUCCESS", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("FIALI", response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    fun clickActionForm() {
        val roomName = binding.edtNameForm.text.toString().trim()
        val exam = binding.edtExamForm.text.toString().trim()
        val teacher1 = binding.edtTeacher1Form.text.toString().trim()
        val teacher2 = binding.edtTeacher2Form.text.toString().trim()
        val start = binding.tvStart.text.toString()
        val finish = binding.tvFinish.text.toString().trim()
        val date = binding.tvDate.text.toString().trim()
        Log.e("Value", " $start $finish $date ${start.contains("Trống")}")
        if (roomName == "") {
            binding.tilNameForm.error = "Không được để trống tên phòng thi"
        } else if (exam == "") {
            binding.tilNameForm.error = null
            binding.tilExamForm.error = "Không được để trống tên môn thi"
        } else if (teacher1 == "") {
            binding.tilNameForm.error = null
            binding.tilExamForm.error = null
            binding.tilTeacher1Form.error = "Không được để trống tên giám thị 1"
        } else if (teacher2 == "") {
            binding.tilNameForm.error = null
            binding.tilExamForm.error = null
            binding.tilTeacher1Form.error = null
            binding.tilTeacher2Form.error = "Không được để trống tên giám thị 2"
        } else if (teacher1 == teacher2) {
            binding.tilTeacher1Form.error = "2 giám thị không được giống nhau"
            binding.tilTeacher2Form.error = "2 giám thị không được giống nhau"
        } else if (start.contains("Trống") || finish.contains("Trống") || date.contains(
                "Trống"
            )
        ) {
            Toast.makeText(this, "Điền và chọn đầy đủ ngày giờ", Toast.LENGTH_SHORT).show()
        } else {
            var check1 = false
            var check2 = false
            teachersName.forEach { s: String ->
                if (teacher1 == s) {
                    check1 = true
                }
                if (teacher2 == s) {
                    check2 = true
                }
            }
            if (!check1) {
                binding.tilTeacher1Form.error = "Giáo viên không có trong danh sách"
            } else if (!check2) {
                binding.tilTeacher2Form.error = "Giáo viên không có trong danh sách"
            } else {
                binding.tilExamForm.error = null
                binding.tilNameForm.error = null
                binding.tilTeacher1Form.error = null
                binding.tilTeacher2Form.error = null
                if (binding.check == "Tạo") {
                    openPDialog()
                    PushNotification(
                        NotificationData("Có phòng thi mới", "Các giáo viên vào xem!"), TOPIC
                    ).also {
                        sendNotification(it)
                    }
                    val id = FirebaseDatabase.getInstance().reference.push().key
                    val examinationRoom =
                        ExaminationRoom(
                            id!!,
                            roomName,
                            date,
                            start,
                            finish,
                            teacher1,
                            teacher2,
                            exam
                        )
                    FirebaseDatabase.getInstance().reference.child("Examination")
                        .child(id)
                        .setValue(examinationRoom.toMap()).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressDialog!!.dismiss()
                                onBackPressed()
                            }
                        }
                } else {
                    PushNotification(
                        NotificationData("Chỉnh sửa phòng thi", "Các giáo viên vào xem!"), TOPIC
                    ).also {
                        sendNotification(it)
                    }
                    val examinationRoom =
                        ExaminationRoom(
                            idUpdate,
                            roomName,
                            date,
                            start,
                            finish,
                            teacher1,
                            teacher2,
                            exam
                        )
                    FirebaseDatabase.getInstance().reference.child("Examination")
                        .child(idUpdate)
                        .setValue(examinationRoom.toMap()).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onBackPressed()
                            }
                        }
                }
            }

        }
    }

    fun autoCompleteTextView() {
        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, teachersName
            )

        binding.edtTeacher1Form.threshold = 1
        binding.edtTeacher2Form.threshold = 1
        binding.edtTeacher1Form.setAdapter(adapter)
        binding.edtTeacher2Form.setAdapter(adapter)
    }

    fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog =
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val date = if (day < 10 && month >= 9) {
                        String.format(getString(R.string.date1), day, month + 1, year)
                    } else if (day < 10 && month < 9) {
                        String.format(getString(R.string.date2), day, month + 1, year)
                    } else if (day > 10 && month < 9) {
                        String.format(getString(R.string.date3), day, month + 1, year)
                    } else {
                        String.format(getString(R.string.date_normal), day, month + 1, year)
                    }
                    binding.date = date
                }, year, month, day
            )

        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun timeStart() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timePickerDialog =
            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    binding.start = "$hourOfDay giờ $minute phút"
                }, hour, minute, true
            )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun timeFinish() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timePickerDialog =
            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    binding.finish = "$hourOfDay giờ $minute phút"
                }, hour, minute, true
            )
        timePickerDialog.show()
    }

    private fun openPDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        progressDialog?.setContentView(R.layout.dialog_progress)
    }
}