package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Dao
interface AnswerDao {

    @Insert
    fun createAnswer(answer: Answer)

    @Update
    fun updateAnswer(answer: Answer)

    @Delete
    fun deleteAnswer(answer: Answer)

    @Query("SELECT * FROM answers WHERE questionId=:questionId AND validSince<=:time AND validUntil>=:time")
    fun getAnswerForQuestionAtTime(questionId: UUID, time: Calendar) : Answer?

}