package de.lulebe.interviewer.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface InterviewDao {
    @Query("SELECT * FROM interviews")
    fun getAllInterviews() : LiveData<List<Interview>>

    @Insert
    fun createInterview(interview: Interview)

    @Delete
    fun deleteInterview(interview: Interview)
}