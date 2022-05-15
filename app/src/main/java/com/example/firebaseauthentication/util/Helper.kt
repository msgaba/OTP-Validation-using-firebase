package com.example.firebaseauthentication.util

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast

/**
 * Created by Ankita
 */

/** key constants to be used throughout project **/
const val KEY_PHN_NO = "phn_no"
const val KEY_VERIFICATION_ID = "verification_id"

/** extension function to hide views **/
fun View.hide() {
    visibility = GONE
}

/** extension function to display toast message **/
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}