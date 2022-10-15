package com.example.bitfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class ExerciseAdapter(private val entries: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>(){
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    // ViewHolder" object which describes and provides access to all the views within each item row.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView : TextView
        val dateTextView : TextView
        val descrTextView : TextView

        init {
            dayTextView = itemView.findViewById(R.id.dayText)
            dateTextView = itemView.findViewById(R.id.dateText)
            descrTextView = itemView.findViewById(R.id.descrText)
        }
    }


    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.exercise_layout, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ExerciseAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val exercise = entries.get(position)
        // Set item views based on your views and data model
        viewHolder.dayTextView.text = exercise.day
        viewHolder.dateTextView.text = exercise.date
        viewHolder.descrTextView.text = exercise.descr
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return entries.size
    }
}