package de.lulebe.interviewer.background

import android.support.v4.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.AppDatabase
import java.util.*
import java.util.concurrent.TimeUnit
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import de.lulebe.interviewer.InterviewActivity


class NotificationWorker : Worker() {

    companion object {
        const val CHANNEL = "Interview Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications about pending interview questions."

        fun enqueueNextNotification(ctx: Context) {
            val db = AppDatabase.getDatabase(ctx)
            val notifications = db.notificationsDao().getAll()
            val currentTime = Calendar.getInstance()
            val next = notifications.sortedBy {
                it.findNextOccurrenceAfter(currentTime).timeInMillis
            }.first()
            val requestBuilder = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            requestBuilder.setInitialDelay(
                    next.findNextOccurrenceAfter(currentTime).timeInMillis - currentTime.timeInMillis,
                    TimeUnit.MILLISECONDS
            )
            requestBuilder.setInputData(Data.Builder().putString("interviewId", next.interviewId.toString()).build())
            val request = requestBuilder.build()
            val sp = ctx.getSharedPreferences("notifications", Context.MODE_PRIVATE)
            sp.getString("workId", null)?.let { oldId ->
                WorkManager.getInstance().cancelWorkById(UUID.fromString(oldId))
            }
            val spEdit = sp.edit()
            spEdit.putString("workId", request.id.toString())
            spEdit.apply()
            WorkManager.getInstance().enqueue(request)
        }

    }

    override fun doWork(): WorkerResult {
        val interviewId = inputData.getString("interviewId", null)
        interviewId?.let {
            val interviews = AppDatabase.getDatabase(applicationContext).interviewDao()
            val interview = interviews.getInterviewById(UUID.fromString(interviewId))
            val intent = Intent(applicationContext, InterviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra("interviewId", interviewId)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL)
                    .setSmallIcon(R.drawable.ic_question_answer_emptyplaceholder)
                    .setContentTitle(interview.name)
                    .setContentText("Click to answer your questions.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            createNotificationChannel()
            val notificationId = 1
            NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
        }
        enqueueNextNotification(applicationContext)
        return WorkerResult.SUCCESS
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL
            val description = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL, name, importance)
            channel.description = description
            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}