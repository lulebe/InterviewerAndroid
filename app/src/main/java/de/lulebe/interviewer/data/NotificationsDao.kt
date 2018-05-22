package de.lulebe.interviewer.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface NotificationsDao {

    @Query("SELECT * FROM notifications")
    fun getAll() : List<Notifications>

    @Insert
    fun createNotifications(notifications: Notifications)

}