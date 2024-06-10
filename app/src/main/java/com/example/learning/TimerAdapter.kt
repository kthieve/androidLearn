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
                    showNotification(timer)
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
        private fun showNotification(timer: Timer) {
            val intent = Intent(itemView.context, TimerListActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(itemView.context, 0, intent, 0)

            val builder = NotificationCompat.Builder(itemView.context, "timer_channel")
                .setSmallIcon(R.drawable.ic_notification)  // Reference the new icon
                .setContentTitle("${timer.title} Timer Finished")
                .setContentText("Your timer for ${timer.title} has finished.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(itemView.context)) {
                notify(timer.hashCode(), builder.build())
            }
        }
    }
}
