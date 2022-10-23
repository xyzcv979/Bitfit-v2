package com.example.bitfit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class ExerciseFragment : Fragment() {
    private lateinit var entries :MutableList<Exercise>
    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        entries = ArrayList()
        exerciseAdapter = ExerciseAdapter(entries)

        exerciseRecyclerView = view.findViewById(R.id.exercise_recycler_view)
        exerciseRecyclerView.layoutManager = LinearLayoutManager(context)
        exerciseRecyclerView.setHasFixedSize(true)
        exerciseRecyclerView.adapter = exerciseAdapter
        exerciseAdapter = ExerciseAdapter(entries)

        lifecycleScope.launch {
            (activity?.application as ExerciseApplication).db.exerciseDao().getAll().collect { databaseList ->
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

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call the new method within onViewCreated
        addExerciseIntent()
    }

    companion object {
        fun newInstance(): ExerciseFragment {
            return ExerciseFragment()
        }
    }

    private fun addExerciseIntent() {
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

                    exerciseRecyclerView.adapter = exerciseAdapter
                    exerciseRecyclerView.layoutManager = LinearLayoutManager(context)

                    val newExerciseEntity = ExerciseEntity(day = dayTextResult, date = dateTextResult, descr = descrTextResult)
                    lifecycleScope.launch(IO) {
                        (activity?.application as ExerciseApplication).db.exerciseDao().insert(newExerciseEntity)
                    }

                }
            }
        }

        var btn = activity?.findViewById<Button>(R.id.addExerciseBtn)
        btn?.setOnClickListener() {
    //            val imm: InputMethodManager =
    //                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    //            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            // ActivityResultLauncher launches Activity and expects a result back

            // Launch Activity and get back result
            val intent = Intent(context, ExerciseEntryActivity::class.java)
            exerciseEntryActivityResultLauncher.launch(intent)
        }
    }
}