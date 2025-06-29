package com.josecarlos.fittrack

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        //FirebaseApp.initializeApp(this)
    }
}
