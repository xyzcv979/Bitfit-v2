package com.example.bitfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ExerciseEntryActivity : AppCompatActivity() {
    private lateinit var dayEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var descrEditText: EditText
    private lateinit var submitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_entry)

        dayEditText = findViewById(R.id.dayEditText)
        dateEditText = findViewById(R.id.dateEditText)
        descrEditText = findViewById(R.id.descrEditText)
        submitBtn = findViewById(R.id.submitBtn)

        submitBtn.setOnClickListener() {
            val data = Intent()
            data.putExtra("day", dayEditText.text.toString())
            data.putExtra("date", dateEditText.text.toString())
            data.putExtra("descr", descrEditText.text.toString())
            setResult(RESULT_OK, data)
            Log.d("Before finish", descrEditText.text.toString())
            finish()
        }
    }
}