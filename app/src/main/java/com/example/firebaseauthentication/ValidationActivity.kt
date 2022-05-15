package com.example.firebaseauthentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.firebaseauthentication.databinding.ActivityValidationBinding
import com.example.firebaseauthentication.util.KEY_PHN_NO
import com.example.firebaseauthentication.util.KEY_VERIFICATION_ID
import com.example.firebaseauthentication.util.showToast
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Created by Ankita
 */
class ValidationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityValidationBinding

    private lateinit var credential: PhoneAuthCredential
    private var phnNo = ""              // variable to store phone number
    private var verificationId = ""     // variable to store verification id
    private var otpCode = ""            // variable to store OTP entered by user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        extractExtras()
        viewClicks()
        // setting phone number in header text
        binding.subHeader.text = String.format(getString(R.string.msg_text), phnNo)
        viewEditAction()
    }

    /** extracting intent data **/
    private fun extractExtras() {
        intent.extras?.let {
            if (it.containsKey(KEY_PHN_NO)) phnNo = it.getString(KEY_PHN_NO, "")
            if (it.containsKey(KEY_VERIFICATION_ID)) verificationId =
                it.getString(KEY_VERIFICATION_ID, "")
        }
    }

    /** click actions for views **/
    private fun viewClicks() {
        binding.apply {
            // click action of validate OTP button
            validateOtp.setOnClickListener {
                // extracting OTP from views, entered by user
                otpCode = edit1.text.toString() +
                        edit2.text.toString() +
                        edit3.text.toString() +
                        edit4.text.toString() +
                        edit5.text.toString() +
                        edit6.text.toString()
                // request for OTP validation
                credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
                signInWithPhoneAuthCredential(credential)
            }
            // click action of change number button
            changeNumber.setOnClickListener {
                onBackPressed()
            }
            // click action  of back button
            back.setOnClickListener {
                onBackPressed()
            }
        }
    }

    /** edit action for edit views **/
    private fun viewEditAction() {
        binding.apply {
            edit1.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) edit2.requestFocus()
            }
            edit2.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) edit3.requestFocus()
            }
            edit3.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) edit4.requestFocus()
            }
            edit4.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) edit5.requestFocus()
            }
            edit5.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) edit6.requestFocus()
            }
        }
    }

    /** OTP validation callbacks **/
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    /** Sign in success **/
                    ProfileActivity.navigate(this@ValidationActivity, phnNo)
                    finish()
                } else {
                    /** Sign in failed **/
                    this.showToast(getString(R.string.incorrect_otp_error_msg))
                }
            }
    }

    companion object {
        fun navigate(context: Activity, phnNo: String, verificationId: String) {
            val intent = Intent(context, ValidationActivity::class.java)
            intent.putExtra(KEY_PHN_NO, phnNo)
            intent.putExtra(KEY_VERIFICATION_ID, verificationId)
            context.startActivity(intent)
        }
    }
}