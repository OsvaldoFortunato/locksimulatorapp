package com.example.lockappemulation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lockappemulation.databinding.ActivityWelcomeBinding

lateinit var binding: ActivityWelcomeBinding

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras!!.getString("nome")
        binding.textViewWelcome.text = "Benvindo, $bundle".uppercase()

    }
}