package com.josecarlos.fittrack.mvc.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.josecarlos.fittrack.data.FirebaseInstances

object FirebaseInstance {

    fun getInstance_Firestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun getInstance_Auth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}