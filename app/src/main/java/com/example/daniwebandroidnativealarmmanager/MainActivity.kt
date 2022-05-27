package com.example.daniwebandroidnativealarmmanager

import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.Calendar.SECOND

private const val NOTIFICATION_CHANNEL_NAME = "0"
const val NOTIFICATION_CHANNEL_ID = "1"

val compatFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    FLAG_IMMUTABLE
} else {
    0
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        findViewById<Button>(R.id.button_scheduleAlarm).setOnClickListener {
            val sendBroadcastIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                sendBroadcastIntent,
                compatFlags
            )

            with(getSystemService(Context.ALARM_SERVICE) as AlarmManager){
                //Alarm will trigger in 10 seconds
                //You should set the alarm by clicking the Button, and then clears the Activity
                // from the backstack by swiping up on the Recents screen.
                val triggerTime = Calendar.getInstance().apply {
                    add(SECOND, 10)
                }

                val alarmInfo = AlarmManager.AlarmClockInfo(
                    triggerTime.timeInMillis,
                    pendingIntent //The alarm will fire this PendingIntent
                )

                setAlarmClock(
                    alarmInfo,
                    pendingIntent
                )
            }
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            with(getSystemService(NOTIFICATION_SERVICE) as NotificationManager){
                createNotificationChannel(channel)
            }
        }
    }
}