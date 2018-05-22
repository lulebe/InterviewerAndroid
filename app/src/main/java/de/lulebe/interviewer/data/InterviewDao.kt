package de.lulebe.interviewer.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface InterviewDao {
    @Query("SELECT * FROM interviews")
    fun getAllInterviews() : LiveData<List<Interview>>

    @Query("SELECT * FROM interviews WHERE id=:id")
    fun getInterviewById(id: UUID) : Interview

    @Insert
    fun createInterview(interview: Interview)

    @Delete
    fun deleteInterview(interview: Interview)
}