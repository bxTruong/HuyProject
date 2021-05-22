package com.bxtruong.huyproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bxtruong.huyproject.databinding.ActivitySignupBinding
import com.bxtruong.huyproject.model.User
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.signUp = this
    }

    fun clickSignUp() {
        val username = binding.edtUNSignUp.text.toString().trim()
        val password = binding.edtPSSignUp.text.toString().trim()
        val teach = binding.edtTeachSignUp.text.toString().trim()
        val faculty = binding.edtFacultySignUp.text.toString().trim()
        val name = binding.edtNameSignUp.text.toString().trim()
        val teacher = binding.rdTeacher.isChecked

        if (username == "" || password == "" || teach == "" || faculty == "" || name == "") {
            Toast.makeText(
                this,
                "Không được để trống bất kỳ ô nhập thông tin nào",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val user = User(username, password, name, teach, faculty, teacher)
            FirebaseDatabase.getInstance().reference.child("Account")
                .child(username.replace(".", ""))
                .setValue(user.toMap()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }
    }
}