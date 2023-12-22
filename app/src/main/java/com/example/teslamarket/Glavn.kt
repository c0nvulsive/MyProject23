package com.example.teslamarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Glavn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glavn)

        val start: TextView = findViewById(R.id.BtnAddCar)
        start.setOnClickListener{
            try {
                val intent = Intent(this, Adding::class.java)
                startActivity(intent)
                finish()


            }
            catch (e: Exception){

            }
        }
        val katolog: TextView = findViewById(R.id.BtnKatolog)
        katolog.setOnClickListener{
            try {
                val intent = Intent(this, Katolog::class.java)
                startActivity(intent)
                finish()


            }
            catch (e: Exception){

            }
        }
    }
}