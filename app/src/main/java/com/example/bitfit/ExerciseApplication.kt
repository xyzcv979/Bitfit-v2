package com.example.bitfit

import android.app.Application

class ExerciseApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}
