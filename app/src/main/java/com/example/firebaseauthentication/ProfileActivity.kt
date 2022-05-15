package com.example.firebaseauthentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthentication.databinding.ActivityProfileBinding
import com.example.firebaseauthentication.util.KEY_PHN_NO

/**
 * Created by Ankita
 */
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private var phnNo = ""          // variable to store phone number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        extractExtras()
        // setting user name on screen
        binding.userName.text = String.format(getString(R.string.user), phnNo)
        viewClicks()
    }

    /** extracting intent data **/
    private fun extractExtras() {
        intent.extras?.let {
            if (it.containsKey(KEY_PHN_NO)) phnNo = it.getString(KEY_PHN_NO, "")
        }
    }

    /** click actions for views **/
    private fun viewClicks() {
        binding.logOut.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    companion object {
        fun navigate(context: Activity, phnNo: String) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(KEY_PHN_NO, phnNo)
            context.startActivity(intent)
        }
    }
}