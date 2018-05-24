package de.lulebe.interviewer.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

@Dao
interface NotificationDao {

    @Insert
    fun createNotification(notification: Notification)

}