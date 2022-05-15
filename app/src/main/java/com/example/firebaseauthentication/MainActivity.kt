package com.example.firebaseauthentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthentication.databinding.ActivityMainBinding
import com.example.firebaseauthentication.util.hide
import com.example.firebaseauthentication.util.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private var phnNo: String = ""   //variable to store phone number entered by user
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize "callbacks" variable
        firebaseCallback()
        // hiding splash screen after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            binding.splashContainer.splashView.hide()
        }, 3000)
        viewClicks()
    }

    /** click actions for views **/
    private fun viewClicks() {
        binding.apply {
            // click action of next button
            next.setOnClickListener {
                phnNo = binding.phnNumber.text.toString()
                if (validate()) sendOtp()
                else this@MainActivity.showToast(getString(R.string.phn_no_error_msg))
            }
            // click action of back button
            back.setOnClickListener {
                onBackPressed()
            }
        }
    }

    /** validating Phone number entered by user **/
    private fun validate(): Boolean {
        return (phnNo.isNotEmpty() && phnNo.length == 10)
    }

    /** sending request for OTP **/
    private fun sendOtp() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phnNo")                        // Phone number to verify in format "+91xxxxxxxxxx"
            .setTimeout(60L, TimeUnit.SECONDS)          // Timeout and unit
            .setActivity(this)                                 // Activity (for callback binding)
            .setCallbacks(callbacks)                           // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun firebaseCallback() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /** This callback will be invoked in two situations:
                1 - Instant verification. In some cases the phone number can be instantly
                verified without needing to send or enter a verification code.
                2 - Auto-retrieval. On some devices Google Play services can automatically
                detect the incoming verification SMS and perform verification without
                user action.**/
            }
            override fun onVerificationFailed(e: FirebaseException) {
                /** This callback is invoked in an invalid request for verification is made,
                for instance if the the phone number format is not valid.**/
                this@MainActivity.showToast(getString(R.string.sms_error_msg))
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                /** The SMS verification code has been sent to the provided phone number, we
                now need to ask the user to enter the code and then construct a credential
                by combining the code with a verification ID.**/
                ValidationActivity.navigate(this@MainActivity, phnNo, verificationId)
            }
        }
    }
}