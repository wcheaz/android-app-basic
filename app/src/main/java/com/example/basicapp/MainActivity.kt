// Declares the package this file belongs to, matching the namespace in build.gradle.kts
package com.example.basicapp

// Imports Bundle, which holds saved state data when the Activity is recreated (e.g. screen rotation)
import android.os.Bundle

// Imports the Button UI widget class from Android's widget library
import android.widget.Button

// Imports the TextView UI widget class used to display text on screen
import android.widget.TextView

// Imports AppCompatActivity, a compatibility class that provides modern ActionBar
// features while working on older Android versions
import androidx.appcompat.app.AppCompatActivity

// Defines the MainActivity class, which inherits from AppCompatActivity.
// This class represents the single screen the user sees when launching the app.
class MainActivity : AppCompatActivity() {

    // The onCreate method is called by Android when the Activity is first created.
    // savedInstanceState contains any previously saved state, or null if fresh.
    override fun onCreate(savedInstanceState: Bundle?) {

        // Calls the parent class's onCreate to perform its default initialization
        super.onCreate(savedInstanceState)

        // Connects this Activity to the XML layout file at res/layout/activity_main.xml,
        // inflating it into actual UI objects on screen
        setContentView(R.layout.activity_main)

        // Finds the TextView element defined in the XML layout by its ID ("textView")
        // and stores a reference to it so we can update its text from code
        val textView = findViewById<TextView>(R.id.textView)

        // Finds the Button element defined in the XML layout by its ID ("button")
        // and stores a reference to it so we can attach a click listener
        val button = findViewById<Button>(R.id.button)

        // Declares a mutable variable to track how many times the button has been tapped
        var count = 0

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
}
