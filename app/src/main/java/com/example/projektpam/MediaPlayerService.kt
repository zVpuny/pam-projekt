package com.example.projektpam

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder

class MediaPlayerService : Service() {

    private var mMediaPlayer: MediaPlayer? = null
    private var mCountDownTimer: CountDownTimer? = null
    val bi = Intent(COUNTDOWN_BR)
    private var timeLeftWhenPaused: Long? = null
    private var endTime: Long? = null


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val action: String = intent.action!!
        val time = intent.getLongExtra("time", 0)

        when (action) {
            "START" -> {
                mMediaPlayer = MediaPlayer.create(this, R.raw.timer_sound)// initialize it here
                mMediaPlayer?.isLooping = true
                createTimer(time)

            }
            "STOP" -> {
                stopTimer()
                endTime = null
                timeLeftWhenPaused = null
            }
            "PAUSE" -> {
                pauseTimer()
            }
            "RESUME" -> {
                resumeTimer()
            }
        }
        return super.onStartCommand(intent, flags, startId)

        /*return START_NOT_STICKY*/
    }

    private fun resumeTimer() {
        endTime = timeLeftWhenPaused?.plus(System.currentTimeMillis())
        createTimer(timeLeftWhenPaused!!)
    }

    private fun pauseTimer() {
        timeLeftWhenPaused = endTime?.minus(System.currentTimeMillis())
        stopTimer()

    }

    private fun stopTimer() {
        mCountDownTimer?.cancel()
        stopMediaPlayer()

    }

    private fun createTimer(time: Long) {
        endTime = System.currentTimeMillis() + time
        mCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secsUntilFinished = millisUntilFinished / 1000
                bi.putExtra("mins", secsUntilFinished / 60)
                bi.putExtra("secs", secsUntilFinished % 60)
                sendBroadcast(bi)

            }

            override fun onFinish() {
                startMediaPlayer()
                bi.putExtra("secs", 0L)
                bi.putExtra("mins", 0L)
                endTime = null
                timeLeftWhenPaused = null
                sendBroadcast(bi)
            }
        }.start()
    }

    private fun startMediaPlayer() {
        if (mMediaPlayer?.isPlaying == false) {
            mMediaPlayer?.start()
        }
    }

    private fun stopMediaPlayer() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
            mMediaPlayer?.reset()
            mMediaPlayer?.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mCountDownTimer?.cancel()
        mMediaPlayer?.release()
        super.onDestroy()
    }


    companion object {
        const val COUNTDOWN_BR = "package com.example.projektpam.timer.countdown_br"
    }


}
