package de.lulebe.interviewer.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface InterviewUserDao {

    @Insert
    fun createInterviewUser(interviewUser: InterviewUser)

    @Query("SELECT * FROM interview_users WHERE interviewId=:interviewId")
    fun getUsersForInterview(interviewId: UUID) : List<InterviewUser>

}