package com.dindamaylan.handspeed

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dindamaylan.handspeed.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var score = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDownTimer: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60
    private val TAG = MainActivity::class.java.simpleName

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iVtoTap.setOnClickListener { v ->
            val bounceAnimation = loadAnimation(this, R.anim.bounce)
            v.startAnimation(bounceAnimation)
            incrementScore()
        }
        Log.d(TAG, "onCreate called. Score is : $score")

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    private fun restoreGame() {
        val restoreScore = getString(R.string.score, score)
        binding.tvScore.text = restoreScore

        val restoreTime = getString(R.string.time, timeLeft)
        binding.tvTime.text = restoreTime

        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeleftString = getString(R.string.time, timeLeft)
                binding.tvTime.text = timeleftString
            }

            override fun onFinish() {
                endGame()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstance. Saving score is : $score and time limit is : $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }

    private fun incrementScore() {
        score++
        val newScore = getString(R.string.score, score)
        binding.tvScore.text = newScore

        if (!gameStarted) startGame()
    }

    private fun resetGame() {
        score = 0

        val initialScore = getString(R.string.score, score)
        binding.tvScore.text = initialScore

        val initialTimeLeft = getString(R.string.time, 60)
        binding.tvTime.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDownTimer, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeleftString = getString(R.string.time, timeLeft)
                binding.tvTime.text = timeleftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over, score), Toast.LENGTH_LONG).show()
        resetGame()
    }
}