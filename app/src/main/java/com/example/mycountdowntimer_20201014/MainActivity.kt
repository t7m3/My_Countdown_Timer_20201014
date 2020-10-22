package com.example.mycountdowntimer_20201014

import android.content.IntentSender
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    inner class  MyCountDownTimer(millisInFuture: Long, countDownIntervl: Long) : CountDownTimer(millisInFuture, countDownIntervl){
        var isRunning = false

        override fun onTick(millisUntilFinished: Long) {
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            timerText.text = "%1d:%02$02d".format(minute, second)
        }

        override  fun onFinish() {
            timerText.text = "0:00"
            soundPool.play(soundResId, 1.0f, 100f, 0, 0, 1.0f)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText.text = "3:00"
        //val timer = MyCountDownTimer(3 * 60 * 1000, 100)
        val timer = MyCountDownTimer(10 * 1000 + 999, 100)

        playStop.setOnClickListener {
            timer.isRunning = when (timer.isRunning) {
                true ->  {
                    timer.cancel()
                    playStop.setImageResource( R.drawable.ic_baseline_play_arrow_24)
                    false
                }
                false -> {
                    timer.start()
                    playStop.setImageResource( R.drawable.ic_baseline_stop_24)
                    true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        soundPool = SoundPool(2, AudioManager.STREAM_ALARM, 0)
        soundResId = soundPool.load(this, R.raw.bellsound, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
}