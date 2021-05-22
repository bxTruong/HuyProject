package com.bxtruong.huyproject

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bxtruong.huyproject.databinding.ActivityLogin2Binding
import com.bxtruong.huyproject.helper.Helper
import com.bxtruong.huyproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity2 : AppCompatActivity() {
    lateinit var binding : ActivityLogin2Binding
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login2)

        binding.login=this
    }

    fun login() {
        val username = binding.edtUserName.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        if (username == "") {
            binding.tilUserName.error ="Không được để trống tài khoản"
        } else if (password == "") {
            binding.tilUserName.error =null
            binding.tilPassword.error ="Không được để trống mật khẩu"
        } else {
            binding.tilUserName.error =null
            binding.tilPassword.error=null
            FirebaseDatabase.getInstance().reference.child("Account").child(username)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Err","${error.message}")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val user = p0.getValue(User::class.java)
                        if ( user?.username!=username) {
                            Toast.makeText(this@LoginActivity2,"Sai Tài Khoản", Toast.LENGTH_SHORT).show()
                        } else if ( user.password!=password) {
                            Toast.makeText(this@LoginActivity2,"Sai Mật Khẩu", Toast.LENGTH_SHORT).show()
                        } else {
                            val user2 = User(
                                username,
                                password,
                                user.full_name,
                                user.teacher
                            )
                            openPDialog()
                            Handler().postDelayed({
                                progressDialog!!.dismiss()
                                Helper.writeUser(this@LoginActivity2, user2)
                                startActivity(Intent(this@LoginActivity2,MainActivity::class.java))
                            }, 1500)
                        }
                    }
                })
        }
    }

    private fun openPDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        progressDialog?.setContentView(R.layout.dialog_progress)
    }
}