package com.utilman.buzzit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser


class VerifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val tvLogout: TextView = findViewById(R.id.bt_verify__logout)
        val tvUserspace: TextView = findViewById(R.id.tv_verify__userspace)
        val tvBox: TextView = findViewById(R.id.tv_verify__tbox)
        val tvChecking: TextView = findViewById(R.id.tv_verify__checking)

        val cUser = FirebaseAuth.getInstance().currentUser?.uid
        val user = FirebaseAuth.getInstance().currentUser
        val email = user!!.email
        val db = Firebase.firestore

        tvLogout.setOnClickListener() {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            auth.signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val docref = db.collection("users").document("$email")

        tvUserspace.text = application.getSharedPreferences("application", Context.MODE_PRIVATE)
            .getString("LUSER",null)

        docref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        tvBox.text = getString(R.string.verified)
                        tvChecking.text = getString(R.string.user_space)
                        Handler().postDelayed({
                            startActivity(Intent(this, BuzzActivity::class.java))
                        }, 1000)
                    } else {
                        tvBox.text = getString(R.string.not_verified)
                        tvChecking.text = getString(R.string.in_waitlist)
                    }

                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }

    }
}