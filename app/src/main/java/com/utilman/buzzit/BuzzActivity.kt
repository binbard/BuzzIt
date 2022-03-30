package com.utilman.buzzit

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase


class BuzzActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buzz)

        val btA: Button = findViewById(R.id.bt_buzz__A)
        val btB: Button = findViewById(R.id.bt_buzz__B)
        val btC: Button = findViewById(R.id.bt_buzz__C)
        val btD: Button = findViewById(R.id.bt_buzz__D)
        val tvTitle: TextView = findViewById(R.id.tv_buzz__title)
        val tvCq: TextView = findViewById(R.id.tv_buzz__cq)

        var ncq = 0
        var cq = 0
        var ca = 0
        var solv = "A"
        var chosen = "A"

        val db = Firebase.firestore
        val luser = application.getSharedPreferences("application", Context.MODE_PRIVATE)
            .getString("LUSER", null).toString()

        val cp_qdata = db.collection("qdata")
        val cp_users = db.collection("users")

        val dp_ans = cp_qdata.document("ans")
        val dp_ques = cp_qdata.document("ques")
        val dp_qwin = cp_qdata.document("qwin")
        val dp_solv = cp_qdata.document("solv")

        val dp_cudoc = cp_users.document(luser)


        fun getSolv() {
            dp_solv.get()
                .addOnSuccessListener { ds_solv ->
                    solv = ds_solv.get("${cq}").toString()
                }
        }

        fun isCorrect(): Boolean {
            if(ca==0 && chosen==solv){
                Toast.makeText(this,"α",Toast.LENGTH_SHORT).show()
                return true
            }
            else if (chosen==solv){
                Toast.makeText(this,"β",Toast.LENGTH_SHORT).show()
                return true
//                Toast.makeText(this,"You are correct cq${cq} ca${ca} sl${solv} ch${chosen}",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"γ",Toast.LENGTH_SHORT).show()
                return false
            }
        }

        fun putUa() {
            val createdAt = FieldValue.serverTimestamp()
            val udat = hashMapOf(
                    "chosen" to chosen,
                    "time" to createdAt
            )
            if(ca==0 && isCorrect()){
                var winner = hashMapOf(
                    "$cq" to luser
                )
                dp_qwin.update("$cq",luser)
                dp_ans.update("ca",ca+1)
            }
            dp_cudoc.update("$cq",udat)
        }

        fun disableAllButtons() {
            btA.setOnClickListener(null)
            btB.setOnClickListener(null)
            btC.setOnClickListener(null)
            btD.setOnClickListener(null)
        }

        fun oneTimeOption() {
            btA.setBackgroundColor(Color.BLUE)
            btB.setBackgroundColor(Color.BLUE)
            btC.setBackgroundColor(Color.BLUE)
            btD.setBackgroundColor(Color.BLUE)

            btA.setOnClickListener {
                chosen = "A"
                disableAllButtons()
                btA.setBackgroundColor(Color.RED)
                isCorrect()
                putUa()
            }
            btB.setOnClickListener {
                chosen = "B"
                disableAllButtons()
                btB.setBackgroundColor(Color.RED)
                isCorrect()
                putUa()
            }
            btC.setOnClickListener {
                chosen = "C"
                disableAllButtons()
                btC.setBackgroundColor(Color.RED)
                isCorrect()
                putUa()
            }
            btD.setOnClickListener {
                chosen = "D"
                disableAllButtons()
                btD.setBackgroundColor(Color.RED)
                isCorrect()
                putUa()
            }
        }

//        tvTitle.setOnClickListener{
//            oneTimeOption()
//        }


        // listener for current question number
        dp_ques.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Toast.makeText(this,"Check your Internet connection",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                oneTimeOption()
                getSolv()
                cq = snapshot.getLong("cq")?.toInt() ?: 0
                tvCq.text = "$cq"
                if(cq==200) startActivity(Intent(this,ThanksActivity::class.java))
            }
            else {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }

        // listening for answers posted for current question
        dp_ans.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Toast.makeText(this,"Check your Internet connection",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                ca = snapshot.getLong("ca")?.toInt() ?: 0
            } else {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }



//        // listening for correct answers posted
//        dp_solv.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Toast.makeText(this,"Check your Internet connection",Toast.LENGTH_SHORT).show()
//                return@addSnapshotListener
//            }
//            if (snapshot != null && snapshot.exists()) {
//                solv = snapshot.get("${cq}").toString()
//            } else {
//                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
//            }
//        }



//        db.collection("qdata")
//            .document("ques")
//            .get()
//            .addOnSuccessListener { result ->
//                val cq = result.data?.get("cq")
//                if(cq!=null){
//                    tvCq.text = "$cq"
//                }
//                else{
//                    tvCq.text = "Not yet started"
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(this,"CQget Failed",Toast.LENGTH_SHORT).show()
//            }

    }
}