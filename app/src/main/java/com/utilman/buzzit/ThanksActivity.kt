package com.utilman.buzzit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ThanksActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thanks)

        val tvUname: TextView = findViewById(R.id.tv_thanks__uname)

        val luser = application.getSharedPreferences("application", Context.MODE_PRIVATE)
            .getString("LUSER",null)?:""

        tvUname.text = luser
    }
}