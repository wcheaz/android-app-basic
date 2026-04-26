package com.example.basicapp

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class CircleActivity : AppCompatActivity() {

    companion object {
        private var savedColor: Int? = null
    }

    private val colors = listOf(
        0xFFBB86FC, 0xFF6200EE, 0xFF3700B3,
        0xFF03DAC5, 0xFF018786, 0xFFE91E63,
        0xFFFF9800, 0xFF4CAF50, 0xFF2196F3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle)

        val circleView = findViewById<View>(R.id.circleView)
        val buttonChangeColor = findViewById<Button>(R.id.buttonChangeColor)
        val buttonNavigateBack = findViewById<Button>(R.id.buttonNavigateBack)

        savedColor?.let { color ->
            (circleView.background as GradientDrawable).setColor(color)
        }

        buttonChangeColor.setOnClickListener {
            val randomColor = colors[Random.nextInt(colors.size)].toInt()
            savedColor = randomColor
            val drawable = circleView.background as GradientDrawable
            drawable.setColor(randomColor)
        }

        buttonNavigateBack.setOnClickListener {
            finish()
        }
    }
}
