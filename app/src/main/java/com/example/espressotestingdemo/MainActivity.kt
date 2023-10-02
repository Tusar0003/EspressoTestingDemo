package com.example.espressotestingdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.espressotestingdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.continueButton.setOnClickListener {
//            binding.errorTextView.text = binding.userNameEditText.text
//        }

        binding.continueButton.setOnClickListener {
            if (binding.userNameEditText.text.isNullOrBlank()) {
                binding.errorTextView.text = EMPTY_MESSAGE
                return@setOnClickListener
            }

            if (binding.userNameEditText.text.toString() != USERNAME) {
                binding.errorTextView.text = INVALID_MESSAGE
                return@setOnClickListener
            }

            binding.errorTextView.text = ""

            startActivity(
                Intent(
                    this,
                    NewActivity::class.java
                )
            )
        }
    }

    companion object {
        const val EMPTY = ""
        const val USERNAME = "Espresso"
        const val INVALID_USERNAME = "Username"
        const val EMPTY_MESSAGE = "Username is empty!"
        const val INVALID_MESSAGE = "Username is invalid!"
    }
}