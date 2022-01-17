package com.example.projektpam.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projektpam.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        this.supportActionBar?.title = getString(R.string.about_title)
    }
}