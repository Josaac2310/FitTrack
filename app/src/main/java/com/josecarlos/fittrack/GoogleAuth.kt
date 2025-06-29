package com.josecarlos.fittrack

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn

object GoogleAuth {

    fun getCurrentUser(context: Context): Boolean{
        val user = GoogleSignIn.getLastSignedInAccount(context)
        if(user == null){
            return false
        }
        else{
            return true
        }
    }

    fun getUserEmail(context: Context):String{
        val user = GoogleSignIn.getLastSignedInAccount(context)
        if(user == null){
            return ""
        }
        else{
            return user.email.toString()
        }
    }

    fun getUserPhoto(context: Context):String{

        val user = GoogleSignIn.getLastSignedInAccount(context)
        if(user == null){
            return ""
        }
        else{
            return user.photoUrl.toString()
        }
    }

}