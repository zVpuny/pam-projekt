package com.example.projektpam

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.projektpam.about.AboutActivity
import com.example.projektpam.shoppinglist.ShoppingListActivity
import com.example.projektpam.timer.TimerActivity
import com.example.projektpam.unitcalc.UnitCalcActivity


class MainActivity : AppCompatActivity() {
    private lateinit var shoppingListBtn: Button
    private lateinit var timerBtn: Button
    private lateinit var unitCalcBtn: Button
    private lateinit var aboutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()

        getButtons()
        setListeners()
    }

    private fun getButtons() {
        shoppingListBtn = findViewById(R.id.shoppingListMainMenuBtn)
        timerBtn = findViewById(R.id.timerMainMenuBtn)
        unitCalcBtn = findViewById(R.id.unitCalcMainMenuBtn)
        aboutBtn = findViewById(R.id.aboutMainMenuBtn)
    }

    private fun setListeners() {
        shoppingListBtn.setOnClickListener {view -> shopingListBtnOnClickListener(view) }
        timerBtn.setOnClickListener {view -> timerBtnOnClickListener(view) }
        unitCalcBtn.setOnClickListener { view -> unitCalcOnClickListener(view) }
        aboutBtn.setOnClickListener { view -> aboutBtnOnClickListener(view) }
    }

    private fun shopingListBtnOnClickListener(view: View) {
        val intent = Intent(view.context,ShoppingListActivity::class.java)
        view.context.startActivity(intent)
    }

    private fun timerBtnOnClickListener(view:View){
        val intent = Intent(view.context,TimerActivity::class.java)
        view.context.startActivity(intent)
    }
    private fun unitCalcOnClickListener(view:View){
        val intent = Intent(view.context,UnitCalcActivity::class.java)
        view.context.startActivity(intent)
    }
    private fun aboutBtnOnClickListener(view:View){
        val intent = Intent(view.context,AboutActivity::class.java)
        view.context.startActivity(intent)
    }

    override fun onStop() {
        this.getSharedPreferences("preferences",MODE_PRIVATE).edit().clear().apply()
        val intent = Intent(this, MediaPlayerService::class.java)
        stopService(intent)
        super.onStop()

    }
}