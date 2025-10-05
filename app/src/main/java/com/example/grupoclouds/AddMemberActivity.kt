package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class AddMemberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        val btnClose = findViewById<ImageView>(R.id.btn_close_miembro)
        btnClose.setOnClickListener {
            startActivity(Intent(this, MiembrosActivity::class.java))
            finish()
        }
    }
}