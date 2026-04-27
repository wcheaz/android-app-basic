package com.example.basicapp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GrowthActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "growth_prefs"
        private const val KEY_VALUE = "growth_value"
        private const val KEY_EXPONENT = "growth_exponent"
        private const val KEY_RATE_INDEX = "growth_rate_index"
        private const val KEY_THEME = "selected_theme"
        private const val INITIAL_VALUE = 100_000_000L
        private const val INITIAL_EXPONENT = 16
        private const val DEFAULT_RATE_INDEX = 2
        private const val RESCALE_THRESHOLD = 100_000_000_000_000_000L
        private const val TICK_MS = 100L
        private const val SAVE_INTERVAL = 10

        private val RATES = listOf(
            100_000L to "0.001%",
            40_000L to "0.0025%",
            20_000L to "0.005%",
            10_000L to "0.01%",
            5_000L to "0.02%",
        )
    }

    private var value = INITIAL_VALUE
    private var exponent = INITIAL_EXPONENT
    private var rateIndex = DEFAULT_RATE_INDEX
    private var selectedTheme = "default"
    private var tickCount = 0
    private lateinit var numberDisplay: TextView
    private lateinit var rateLabel: TextView
    private lateinit var btnSlower: Button
    private lateinit var btnFaster: Button
    private val handler = Handler(Looper.getMainLooper())

    private val tick = object : Runnable {
        override fun run() {
            value += value / RATES[rateIndex].first
            if (value > RESCALE_THRESHOLD) {
                value /= 1000L
                exponent -= 3
            }
            numberDisplay.text = formatNumber(value, exponent)
            tickCount++
            if (tickCount % SAVE_INTERVAL == 0) saveState()
            handler.postDelayed(this, TICK_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadState()
        setTheme(themeKeyToStyleRes(selectedTheme))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growth)
        supportActionBar?.hide()

        numberDisplay = findViewById(R.id.growthNumber)
        rateLabel = findViewById(R.id.rateLabel)
        btnSlower = findViewById(R.id.btnSlower)
        btnFaster = findViewById(R.id.btnFaster)

        btnSlower.setOnClickListener {
            if (rateIndex > 0) {
                rateIndex--
                updateRateControls()
                saveState()
            }
        }

        btnFaster.setOnClickListener {
            if (rateIndex < RATES.lastIndex) {
                rateIndex++
                updateRateControls()
                saveState()
            }
        }

        updateRateControls()
        numberDisplay.text = formatNumber(value, exponent)
    }

    override fun onResume() {
        super.onResume()
        handler.post(tick)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(tick)
        saveState()
    }

    private fun updateRateControls() {
        rateLabel.text = "${RATES[rateIndex].second} / tick"
        btnSlower.isEnabled = rateIndex > 0
        btnFaster.isEnabled = rateIndex < RATES.lastIndex
    }

    private fun loadState() {
        val p = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        value = p.getLong(KEY_VALUE, INITIAL_VALUE)
        exponent = p.getInt(KEY_EXPONENT, INITIAL_EXPONENT)
        rateIndex = p.getInt(KEY_RATE_INDEX, DEFAULT_RATE_INDEX).coerceIn(0, RATES.lastIndex)
        selectedTheme = p.getString(KEY_THEME, "default") ?: "default"
    }

    private fun saveState() {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putLong(KEY_VALUE, value)
            .putInt(KEY_EXPONENT, exponent)
            .putInt(KEY_RATE_INDEX, rateIndex)
            .putString(KEY_THEME, selectedTheme)
            .apply()
    }

    private fun themeKeyToStyleRes(key: String): Int = when (key) {
        "default" -> R.style.Theme_BasicApp
        "minimalist_noir" -> R.style.Theme_BasicApp_Noir
        else -> R.style.Theme_BasicApp
    }

    private fun formatNumber(v: Long, exp: Int): String {
        var s = v.toString()
        if (exp <= 0) {
            if (exp < 0) repeat(-exp) { s += "0" }
            return s
        }
        val formatted = if (s.length <= exp) {
            "0.${"0".repeat(exp - s.length)}$s"
        } else {
            "${s.substring(0, s.length - exp)}.${s.substring(s.length - exp)}"
        }
        return formatted.trimEnd('0').trimEnd('.')
    }
}
