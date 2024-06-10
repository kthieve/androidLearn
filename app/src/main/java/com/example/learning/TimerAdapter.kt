// src/main/java/com/example/myapp/TimerAdapter.kt
package com.example.learning

import android.app.PendingIntent
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView

class TimerAdapter(private val timers: List<Timer>) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timer, parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val timer = timers[position]
        holder.bind(timer)
    }

    override fun getItemCount(): Int = timers.size

    inner class TimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvRepeat: TextView = itemView.findViewById(R.id.tvRepeat)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val tvTimeLeft: TextView = itemView.findViewById(R.id.tvTimeLeft)

        fun bind(timer: Timer) {
            tvTitle.text = timer.title
            tvCategory.text = timer.category
            tvRepeat.text = if (timer.repeat) "Repeat" else "One-time"
            progressBar.max = timer.duration.toInt()

            val countDownTimer = object : CountDownTimer(timer.duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsLeft = millisUntilFinished / 1000
                    tvTimeLeft.text = "$secondsLeft seconds left"
                    progressBar.progress = millisUntilFinished.toInt()
                }

                override fun onFinish() {
                    progressBar.progress = 0
                    Toast.makeText(itemView.context, "${timer.title} timer finished", Toast.LENGTH_SHORT).show()
                    tvTimeLeft.text = "Finished"
                    itemView.isEnabled = !timer.repeat
                    if (timer.repeat) {
                        start()
                    } else {
                        progressBar.progress = 0
                    }
                }
            }

            countDownTimer.start()

            itemView.setOnClickListener {
                if (timer.repeat) {
                    countDownTimer.start()
                } else {
                    itemView.isClickable = false
                }
            }
        }

    }
}
