// Declares the package this file belongs to, matching the namespace in build.gradle.kts
package com.example.basicapp

// Imports Bundle, which holds saved state data when the Activity is recreated (e.g. screen rotation)
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

// Imports AppCompatActivity, a compatibility class that provides modern ActionBar
// features while working on older Android versions
import androidx.appcompat.app.AppCompatActivity

// Defines the MainActivity class, which inherits from AppCompatActivity.
// This class represents the single screen the user sees when launching the app.
class MainActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)

        val button = findViewById<Button>(R.id.button)
        val buttonNavigateToCircle = findViewById<Button>(R.id.buttonNavigateToCircle)

        buttonNavigateToCircle.setOnClickListener {
            val intent = Intent(this, CircleActivity::class.java)
            startActivity(intent)
        }

        count = savedInstanceState?.getInt("count") ?: 0

        // Attaches a click listener to the button. The code inside the braces
        // runs every time the user taps the button.
        button.setOnClickListener {

            // Increments the tap counter by 1 each time the button is pressed
            count++

            // Updates the TextView's displayed text with the current count.
            // Uses Kotlin string interpolation ($count) to embed the number.
            // The ternary-like expression adds an "s" for plural (e.g. "2 times" vs "1 time").
            textView.text = "You tapped the button $count time${if (count != 1) "s" else ""}!"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
    }
}
