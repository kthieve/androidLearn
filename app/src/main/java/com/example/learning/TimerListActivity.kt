//TimerListActivity.kt
package com.example.learning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

public object TimerList {

    private val timers = mutableListOf<Timer>()

    fun addTimer(timer: Timer) {
        timers.add(timer)
    }

    fun getTimers(): List<Timer> {
        return timers
    }

}


class TimerListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_list)

        val rvTimers = findViewById<RecyclerView>(R.id.rvTimers)
        rvTimers.layoutManager = LinearLayoutManager(this)




        val timers = TimerList.getTimers()

        val adapter = TimerAdapter(timers)
        rvTimers.adapter = adapter

        // Setup "+" button to open NewCooldownActivity
        val addButton = findViewById<Button>(R.id.btn_add_cooldown)
        addButton.setOnClickListener {
            val intent = Intent(this, NewCooldownActivity::class.java)
            startActivity(intent)
        }
    }
}
