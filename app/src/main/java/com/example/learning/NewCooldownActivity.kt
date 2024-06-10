package com.example.learning

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class NewCooldownActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etCategory: EditText
    private lateinit var etDays: EditText
    private lateinit var etHours: EditText
    private lateinit var etMinutes: EditText
    private lateinit var etSeconds: EditText
    private lateinit var btnDateTimeSelector: Button
    private lateinit var cbRepeat: CheckBox
    private lateinit var btnCreate: Button
    private lateinit var btnCancel: Button
    private var cooldown: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_cooldown)

        etTitle = findViewById(R.id.etTitle)
        etCategory = findViewById(R.id.etCategory)
        etDays = findViewById(R.id.etDays)
        etHours = findViewById(R.id.etHours)
        etMinutes = findViewById(R.id.etMinutes)
        etSeconds = findViewById(R.id.etSeconds)
        btnDateTimeSelector = findViewById(R.id.btnDateTimeSelector)
        cbRepeat = findViewById(R.id.cbRepeat)
        btnCreate = findViewById(R.id.btnCreate)
        btnCancel = findViewById(R.id.btnCancel)

        btnDateTimeSelector.setOnClickListener {
            showDateTimePicker()
        }

        btnCreate.setOnClickListener {
            createCooldown()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(year, month, dayOfMonth, hourOfDay, minute)

                // Calculate the duration
                val currentTimeMillis = System.currentTimeMillis()
                val selectedTimeMillis = calendar.timeInMillis
                val differenceMillis = selectedTimeMillis - currentTimeMillis

                val days = differenceMillis / (1000 * 60 * 60 * 24)
                val hours = (differenceMillis / (1000 * 60 * 60)) % 24
                val minutes = (differenceMillis / (1000 * 60)) % 60
                val seconds = (differenceMillis / 1000) % 60

                // Display the duration
                etDays.setText(days.toString())
                etHours.setText(hours.toString())
                etMinutes.setText(minutes.toString())
                etSeconds.setText(seconds.toString())

                // Update the cooldown value
                cooldown = differenceMillis
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun createCooldown() {
        val title = etTitle.text.toString()
        val category = etCategory.text.toString()
        val repeat = cbRepeat.isChecked

        // Create a new timer and add it to the timer list
        val newTimer = Timer(title, category, cooldown, repeat)
        TimerList.addTimer(newTimer)

        // Show a toast message
        Toast.makeText(this, "Cooldown created", Toast.LENGTH_SHORT).show()

        // Go back to the timer list activity
        val intent = Intent(this, TimerListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
