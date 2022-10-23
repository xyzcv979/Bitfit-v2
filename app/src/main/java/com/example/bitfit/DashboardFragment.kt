package com.example.bitfit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class DashboardFragment : Fragment() {
    private lateinit var entries : MutableList<Exercise>
    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter

    lateinit var monVal : TextView
    lateinit var tuesVal : TextView
    lateinit var wednesVal : TextView
    lateinit var thursVal : TextView
    lateinit var friVal : TextView
    lateinit var satVal : TextView
    lateinit var sunVal  : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        monVal = view.findViewById<TextView>(R.id.textView8)
        tuesVal = view.findViewById<TextView>(R.id.textView9)
        wednesVal = view.findViewById<TextView>(R.id.textView10)
        thursVal = view.findViewById<TextView>(R.id.textView11)
        friVal = view.findViewById<TextView>(R.id.textView12)
        satVal = view.findViewById<TextView>(R.id.textView13)
        sunVal = view.findViewById<TextView>(R.id.textView14)


        entries = ArrayList()
        retrieveExerciseData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call the new method within onViewCreated
        addExerciseIntent()
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    private fun retrieveExerciseData() {

        var newMonVal = 0
        var newTuesVal = 0
        var newWednesVal = 0
        var newThursVal = 0
        var newfFriVal = 0
        var newSatVal = 0
        var newSunVal = 0

        lifecycleScope.launch(IO) {
            (activity?.application as ExerciseApplication).db.exerciseDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    Exercise(
                        entity.day,
                        entity.date,
                        entity.descr
                    )
                }.also { mappedList ->
                    entries.addAll(mappedList)
                }
            }
        }

        while(entries.isNullOrEmpty()) {

        }

        if(!entries.isNullOrEmpty()) {
            for(exercise in entries) {
                var currDay = exercise.day?.lowercase()
                if(currDay == "monday") {
                    newMonVal++
                } else if(currDay == "tuesday") {
                    newTuesVal++
                } else if(currDay == "wednesday") {
                    newWednesVal++
                } else if(currDay == "thursday") {
                    newThursVal++
                } else if(currDay == "friday") {
                    newfFriVal++
                } else if(currDay == "saturday") {
                    newSatVal++
                }else if(currDay == "sunday") {
                    newSunVal++
                }
            }

            monVal.text = newMonVal.toString()
            tuesVal.text = newTuesVal.toString()
            wednesVal.text = newWednesVal.toString()
            thursVal.text = newThursVal.toString()
            friVal.text = newfFriVal.toString()
            satVal.text = newSatVal.toString()
            sunVal.text = newSunVal.toString()
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
                    lifecycleScope.launch(Dispatchers.IO) {
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