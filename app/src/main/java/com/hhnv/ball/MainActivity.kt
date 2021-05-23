package com.hhnv.ball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import com.hhnv.ball.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), Runnable {
    private var squareDegree: Float = 0f
    private var score = 0
    private var ballColor = 0
    private var squareColor = 0
    private val FALL_DURATION: Long = 2000
    private var handler = Handler()
    private var isRunning = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRight.setOnClickListener {
            rotateSquareToRight()
        }

        binding.btnLeft.setOnClickListener {
            rotateSquareToLeft()
        }

        binding.imgStart.setOnClickListener {
            starGame()
        }

        Thread(this).start()
    }

    private fun starGame() {
        isRunning = true
        score = 0
        binding.tvScore.text = "0"
        binding.imgStart.visibility = View.INVISIBLE
        ballColor = 0
        squareColor = 0
        binding.imgSquare.clearAnimation()
    }


    private fun rotateSquareToRight() {
        val rotateAnimation = RotateAnimation(
            squareDegree, squareDegree + 90,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 250
            fillAfter = true
        }
        squareDegree += 90
        squareColor--

        if (squareColor < 0) {
            squareColor = 3
        }

        binding.imgSquare.startAnimation(rotateAnimation)
    }

    private fun rotateSquareToLeft() {
        val rotateAnimation = RotateAnimation(
            squareDegree, squareDegree - 90,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 250
            fillAfter = true
        }
        squareDegree -= 90
        squareColor++

        if (squareColor > 3) {
            squareColor = 0
        }

        binding.imgSquare.startAnimation(rotateAnimation)
    }

    private fun ballFall() {
        ballColor = Random().nextInt(4)
        binding.imgBall.setImageResource(R.drawable.ball_0 + ballColor)
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0.9f
        ).apply {
            duration = FALL_DURATION
        }
        binding.imgBall.startAnimation(animation)
    }

    override fun run() {
        while (true) {
            if (isRunning) {
                ballFall()
                Thread.sleep(FALL_DURATION)
                if (squareColor == ballColor) {
                    score++
                    handler.post() {
                        binding.tvScore.text = score.toString()
                    }
                    Log.d("HieuNV", "square: " + squareColor )
                    Log.d("HieuNV", "ball: " + ballColor )
                } else {
                    isRunning = false
                    handler.post() {
                        binding.imgStart.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}