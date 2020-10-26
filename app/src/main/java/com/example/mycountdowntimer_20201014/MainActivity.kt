package com.example.mycountdowntimer_20201014

import android.content.IntentSender
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    //private var dir = 1  //アニメーション用の変数の宣言
    private var step = 5  //アニメーション用の変数の宣言
    private var screenWidth = 0  //スクリーンの幅を格納する変数の宣言
    private var screenHeight = 0   //スクリーンの高さ格納する変数の宣言

    inner class  MyCountDownTimer(millisInFuture: Long, countDownIntervl: Long) : CountDownTimer(millisInFuture, countDownIntervl){
        var isRunning = false

        override fun onTick(millisUntilFinished: Long) {
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            timerText.text = "%1d:%02$02d".format(minute, second)

            // imageViewEnemyを左右に移動する
            imageViewEnemy.x = imageViewEnemy.x + step

            if( imageViewEnemy.x > screenWidth - imageViewEnemy.width){  // 右端で移動する向きを左に変える
                step = -5
            }
            else if( imageViewEnemy.x < 0){  // 左端で移動する向きを右に変える
                step = +5
            }
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
        val timer = MyCountDownTimer(10 * 60 * 1000 + 999, 100)

        // スクリーンの幅と高さを取得する
        val dMetrics = DisplayMetrics()  //DisplayMetrics のインスタンスを生成する
        windowManager.defaultDisplay.getMetrics(dMetrics)  //スクリーンサイズを取得しているらしい
        screenWidth = dMetrics.widthPixels  //スクリーンの幅を取得
        screenHeight = dMetrics.heightPixels  //スクリーンの高さを取得

        // imageViewEnemyの初期位置の設定
        imageViewEnemy.x = 10F
        imageViewEnemy.y = 100F

        // mageViewPlayer の初期位置の設定
        imageViewPlayer.x = 50F
        imageViewPlayer.y = screenHeight.toFloat() * 0.6F

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


    //画面タッチのメソッドの定義
    override fun onTouchEvent(event: MotionEvent): Boolean {

        textView.text = "X座標：${event.x}　Y座標：${event.y}"

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                textView.append("　ダウン")
                imageViewPlayer.x = event.x
            }

            MotionEvent.ACTION_UP -> textView.append("　アップ")

            MotionEvent.ACTION_MOVE -> {
                textView.append("　ムーブ")
                imageViewPlayer.x = event.x
            }

            MotionEvent.ACTION_CANCEL -> textView.append("　キャンセル")
        }

        return true

    }
}