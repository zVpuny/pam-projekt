package com.example.projektpam.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.projektpam.MediaPlayerService
import com.example.projektpam.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TimerActivity : AppCompatActivity() {
    var minutes: Int = 0
    var seconds: Int = 0
    var endTime: Long = 0
    var timeLeftWhenPaused: Long = 0
    var timerRunning = false
    var timerPaused = false

    private lateinit var minutesTextView: TextView
    private lateinit var secondsTextView: TextView

    lateinit var extractMinutesBtn: FloatingActionButton
    lateinit var addMinutesBtn: FloatingActionButton
    lateinit var extractSecondsBtn: FloatingActionButton
    lateinit var addSecondsBtn: FloatingActionButton

    lateinit var setTimerButton: Button
    lateinit var pauseTimerButton: Button
    lateinit var stopTimerButton: Button
    lateinit var resumeTimerButton: Button

    private var timerState: TimerState = TimerState.STOPPED

    private var br = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateView(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        this.supportActionBar?.title = getString(R.string.timer_title_bar)
        this.supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this,R.color.primaryLightColor))
        this.supportActionBar?.elevation = 0f
        setTimerButton = findViewById(R.id.setUpTimerButton)
        setTimerButton.setOnClickListener { view -> acceptTime(view) }

        pauseTimerButton = findViewById(R.id.pauseTimerButton)
        pauseTimerButton.setOnClickListener {
            pauseTimer()
        }

        stopTimerButton = findViewById(R.id.stopTimerButton)
        stopTimerButton.visibility = View.VISIBLE
        stopTimerButton.setOnClickListener {
            val intent = Intent(this, MediaPlayerService::class.java)
            intent.action = "STOP"
            startService(intent)

            timerState = TimerState.STOPPED

            timerStoppedUI()

            seconds = 0
            minutes = 0
            secondsTextView.text = "0"
            minutesTextView.text = "0"

            timerPaused = false
            timerRunning = false
        }

        resumeTimerButton = findViewById(R.id.resumeTimerButton)
        resumeTimerButton.setOnClickListener {
            val intent = Intent(this, MediaPlayerService::class.java)
            intent.action = "RESUME"
            startService(intent)

            timerState = TimerState.RUNNING

            timerRunningUI()
        }

        extractMinutesBtn = findViewById(R.id.minutesExtractBtn)
        addMinutesBtn = findViewById(R.id.minutesAddBtn)
        extractSecondsBtn = findViewById(R.id.secondsExtractBtn)
        addSecondsBtn = findViewById(R.id.secondsAddBtn)

        secondsTextView = findViewById(R.id.secondsTextView)
        minutesTextView = findViewById(R.id.minutesTextView)

        updateTextViews()


        extractMinutesBtn.setOnClickListener {
            if (minutes > 0) {
                minutes--;
            } else {
                minutes = 59
            }
            updateTextViews()
        }

        extractSecondsBtn.setOnClickListener {
            if (seconds > 0) {
                seconds--
            } else {
                seconds = 59
            }
            updateTextViews()
        }

        addMinutesBtn.setOnClickListener {
            if (minutes < 59) {
                minutes++;
            } else {
                minutes = 0
            }
            updateTextViews()
        }

        addSecondsBtn.setOnClickListener {
            if (seconds < 59) {
                seconds++;
            } else {
                seconds = 0
            }
            updateTextViews()
        }
    }

    private fun updateView(intent: Intent?) {
        seconds = intent?.getLongExtra("secs", 0)!!.toInt()
        minutes = intent?.getLongExtra("mins", 0)!!.toInt()
        if (minutes == 0 && seconds == 0) {
            pauseTimerButton.visibility = View.GONE
        }
        updateTextViews()
    }

    private fun acceptTime(view: View?) {
        val time: Int = minutes * 60 * 1000 + seconds * 1000
        endTime = System.currentTimeMillis() + time
        setUpTimer(time.toLong())
    }

    private fun setUpTimer(time: Long) {
        endTime = System.currentTimeMillis() + time

        val intent = Intent(this, MediaPlayerService::class.java)
        intent.action = "START"
        intent.putExtra("time", time)
        startService(intent)

        timerState = TimerState.RUNNING

        timerRunningUI()
    }

    private fun updateTextViews() {
        minutesTextView.text = minutes.toString()
        secondsTextView.text = seconds.toString()

    }

    private fun savePreferences() {
        val sharedPreferences = this.getSharedPreferences("preferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("timerState", timerState.name)
            putInt("mins", minutes)
            putInt("secs", seconds)
            commit()
        }
    }

    private fun loadPreferences() {

        val preferences = this.getSharedPreferences("preferences", MODE_PRIVATE)
        timerState =
            TimerState.valueOf(preferences.getString("timerState", TimerState.STOPPED.name)!!)
        minutes = preferences.getInt("mins", 0)
        seconds = preferences.getInt("secs", 0)
        updateTextViews()
        if (minutes == 0 && seconds == 0) {
            pauseTimerButton.visibility = View.GONE
        }
        setUpUI(timerState)
    }

    private fun setUpUI(timerState: TimerState) {
        when (timerState) {
            TimerState.STOPPED -> {
                timerStoppedUI()
            }
            TimerState.PAUSED -> {
                timerPausedUI()
            }
            TimerState.RUNNING -> {
                timerRunningUI()
            }
        }
    }

    private fun timerRunningUI() {
        hideTimeChangeButtons()

        setTimerButton.visibility = View.GONE
        pauseTimerButton.visibility = View.VISIBLE
        resumeTimerButton.visibility = View.GONE
        stopTimerButton.visibility = View.VISIBLE
    }

    private fun timerPausedUI() {
        hideTimeChangeButtons()

        setTimerButton.visibility = View.GONE
        pauseTimerButton.visibility = View.GONE
        resumeTimerButton.visibility = View.VISIBLE
        stopTimerButton.visibility = View.VISIBLE

    }

    private fun hideTimeChangeButtons() {
        extractSecondsBtn.visibility = View.INVISIBLE
        addSecondsBtn.visibility = View.INVISIBLE
        extractMinutesBtn.visibility = View.INVISIBLE
        addMinutesBtn.visibility = View.INVISIBLE
    }

    private fun timerStoppedUI() {
        extractSecondsBtn.visibility = View.VISIBLE
        addSecondsBtn.visibility = View.VISIBLE
        extractMinutesBtn.visibility = View.VISIBLE
        addMinutesBtn.visibility = View.VISIBLE

        setTimerButton.visibility = View.VISIBLE
        pauseTimerButton.visibility = View.GONE
        resumeTimerButton.visibility = View.GONE
        stopTimerButton.visibility = View.GONE

    }

    private fun pauseTimer() {
        timeLeftWhenPaused = endTime - System.currentTimeMillis()
        //timer.cancel()
        val intent = Intent(this, MediaPlayerService::class.java)
        intent.action = "PAUSE"
        startService(intent)
        timerState = TimerState.PAUSED

        timerPausedUI()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(br, IntentFilter(MediaPlayerService.COUNTDOWN_BR))
        loadPreferences()
    }

    override fun onPause() {
        savePreferences()
        super.onPause()
        unregisterReceiver(br)
    }

    override fun onStop() {
        savePreferences()
        try {
            unregisterReceiver(br)
        } catch (e: Exception) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }


    enum class TimerState {
        STOPPED, RUNNING, PAUSED
    }


}