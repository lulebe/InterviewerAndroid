package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Dao
interface ScheduleDao {

    @Insert
    fun createSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedules WHERE interviewId=:interviewId LIMIT 0,1")
    fun getScheduleForInterview(interviewId: UUID) : Schedule

}