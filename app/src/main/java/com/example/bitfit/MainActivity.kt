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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
//    lateinit var entries :MutableList<Exercise>
//    lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Call helper method to swap the FrameLayout with the fragment
        replaceFragment(ExerciseFragment())

        // Swapping fragments when clicking button nav view
        val fragmentManager: FragmentManager = supportFragmentManager

        // define your fragments here
        val exerciseFragment: Fragment = ExerciseFragment()
        val dashboardFragment: Fragment = DashboardFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.workout_list -> fragment = exerciseFragment
                R.id.dashboard -> fragment = dashboardFragment
            }
            fragmentManager.beginTransaction().replace(R.id.exercise_frame_layout, fragment).commit()
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.workout_list


//        val recyclerView = findViewById<RecyclerView>(R.id.exercise_recycler_view)
////
//        val addExerciseBtn = findViewById<Button>(R.id.addExerciseBtn)
//
//        entries = ArrayList()
//
//        exerciseAdapter = ExerciseAdapter(entries)
//        recyclerView.adapter = exerciseAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this)

//        lifecycleScope.launch {
//            (application as ExerciseApplication).db.exerciseDao().getAll().collect { databaseList ->
//                databaseList.map { entity ->
//                    Exercise(
//                        entity.day,
//                        entity.date,
//                        entity.descr
//                    )
//                }.also { mappedList ->
//                    entries.clear()
//                    entries.addAll(mappedList)
//                    exerciseAdapter.notifyDataSetChanged()
//                }
//            }
//        }

//        var exerciseEntryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            // If the user comes back to this activity from EditActivity
//            // with no error or cancellation
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data
//                // Get the data passed from EditActivity
//                if (data != null) {
//                    var dayTextResult = data.extras!!.getString("day")!!
//                    var dateTextResult = data.extras!!.getString("date")!!
//                    var descrTextResult = data.extras!!.getString("descr")!!
//
//                    val newExercise = Exercise(dayTextResult, dateTextResult, descrTextResult)
//
//                    entries.add(newExercise)
//
//                    exerciseAdapter = ExerciseAdapter(entries)
//
//                    recyclerView.adapter = exerciseAdapter
//                    recyclerView.layoutManager = LinearLayoutManager(this)
//
//                    val newExerciseEntity = ExerciseEntity(day = dayTextResult, date = dateTextResult, descr = descrTextResult)
//                    lifecycleScope.launch(IO) {
//                        (application as ExerciseApplication).db.exerciseDao().insert(newExerciseEntity)
//                    }
//
//                }
//            }
//        }
//
//        addExerciseBtn.setOnClickListener() {
////            val imm: InputMethodManager =
////                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
////            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//            // ActivityResultLauncher launches Activity and expects a result back
//
//            // Launch Activity and get back result
//            val intent = Intent(this@MainActivity, ExerciseEntryActivity::class.java)
//            exerciseEntryActivityResultLauncher.launch(intent)
//        }
    }

    private fun replaceFragment(exerciseFragment: ExerciseFragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.exercise_frame_layout, exerciseFragment)
        fragmentTransaction.commit()
    }

}