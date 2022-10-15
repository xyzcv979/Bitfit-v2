package com.example.bitfit

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var entries :MutableList<Exercise>
    lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val addExerciseBtn = findViewById<Button>(R.id.addExerciseBtn)

        entries = ArrayList()

        exerciseAdapter = ExerciseAdapter(entries)
        recyclerView.adapter = exerciseAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            (application as ExerciseApplication).db.exerciseDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    Exercise(
                        entity.day,
                        entity.date,
                        entity.descr
                    )
                }.also { mappedList ->
                    entries.clear()
                    entries.addAll(mappedList)
                    exerciseAdapter.notifyDataSetChanged()
                }
            }
        }

        var exerciseEntryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // If the user comes back to this activity from EditActivity
            // with no error or cancellation
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                // Get the data passed from EditActivity
                if (data != null) {
                    var dayTextResult = data.extras!!.getString("day")!!
                    var dateTextResult = data.extras!!.getString("date")!!
                    var descrTextResult = data.extras!!.getString("descr")!!

                    val newExercise = Exercise(dayTextResult, dateTextResult, descrTextResult)

                    entries.add(newExercise)

                    exerciseAdapter = ExerciseAdapter(entries)

                    recyclerView.adapter = exerciseAdapter
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    val newExerciseEntity = ExerciseEntity(day = dayTextResult, date = dateTextResult, descr = descrTextResult)
                    lifecycleScope.launch(IO) {
                        (application as ExerciseApplication).db.exerciseDao().insert(newExerciseEntity)
                    }

                }
            }
        }

        addExerciseBtn.setOnClickListener() {
//            val imm: InputMethodManager =
//                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            // ActivityResultLauncher launches Activity and expects a result back

            // Launch Activity and get back result
            val intent = Intent(this@MainActivity, ExerciseEntryActivity::class.java)
            exerciseEntryActivityResultLauncher.launch(intent)



        }

    }
}